package lombok.javac.handlers;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import lombok.HelloWorld;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import org.kohsuke.MetaInfServices;

import java.lang.reflect.Modifier;

@MetaInfServices(JavacAnnotationHandler.class)
@SuppressWarnings("restriction")
public class HandleHelloWorld extends JavacAnnotationHandler<HelloWorld> {

    public void handle(AnnotationValues<HelloWorld> annotation, JCAnnotation ast, JavacNode annotationNode) {
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, HelloWorld.class);
        JavacNode typeNode = annotationNode.up();

        if (notAClass(typeNode)) {
            annotationNode.addError("@HelloWorld is only supported on a class.");
            return;
        }

        JCMethodDecl helloWorldMethod = createHelloWorld(typeNode);
        JavacHandlerUtil.injectMethod(typeNode, helloWorldMethod);
    }

    private boolean notAClass(JavacNode typeNode) {
        JCClassDecl typeDecl = null;
        if (typeNode.get() instanceof JCClassDecl) typeDecl = (JCClassDecl) typeNode.get();
        long flags = typeDecl == null ? 0 : typeDecl.mods.flags;
        return typeDecl == null || (flags & (Flags.INTERFACE | Flags.ENUM | Flags.ANNOTATION)) != 0;
    }

    public boolean isResolutionBased() {
        return false;
    }

    private JCMethodDecl createHelloWorld(JavacNode type) {
        JavacTreeMaker treeMaker = type.getTreeMaker();

        JCModifiers modifiers = treeMaker.Modifiers(Modifier.PUBLIC);
        List<JCTypeParameter> methodGenericTypes = List.<JCTypeParameter>nil();
        JCExpression methodType = treeMaker.TypeIdent(JavacTreeMaker.TypeTag.typeTag(new Type.JCVoidType()));
        Name methodName = type.toName("helloWorld");
        List<JCVariableDecl> methodParameters = List.<JCVariableDecl>nil();
        List<JCExpression> methodThrows = List.<JCExpression>nil();

        JCExpression printlnMethod = JavacHandlerUtil.chainDots(type, "System", "out", "println");
        List<JCExpression> printlnArgs = List.<JCExpression>of(treeMaker.Literal("hello world"));
        JCMethodInvocation printlnInvocation = treeMaker.Apply(List.<JCExpression>nil(), printlnMethod, printlnArgs);
        JCBlock methodBody = treeMaker.Block(0, List.<JCStatement>of(treeMaker.Exec(printlnInvocation)));

        JCExpression defaultValue = null;

        return treeMaker.MethodDef(
                modifiers, methodName, methodType, methodGenericTypes, methodParameters, methodThrows, methodBody, defaultValue);
    }
}
