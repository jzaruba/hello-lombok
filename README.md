 0) rename files on original JAR
 0) use the updated JAR as dependency
 0) do the coding
 0) build the extension
 0) rename the files in it
 0) merge the extension with the original
 0) install the updated library
 0) run tests

In new Lombok the bytecode is delivered in files with *.SCL.lombok suffix, 
instead of the usual *.class.

Rename *.SCL.lombok files in the JAR to *.class:
```
rnm lombok-1.16.18.jar .lombok .
rnm lombok-1.16.18~.jar .SCL .class
```

Install the fixed Lombok JAR so you can use it as dependency in Idea:  
``mvn install:install-file 
-Dfile="lombok-1.16.18~~.jar" 
-DgroupId=net.jzaruba 
-DartifactId=lombok 
-Dversion=1.16.18 
-Dpackaging=jar``

Update/refresh the dependency so Idea can see the *.class files.

The Lombok-extension related files in your project will now successfully compile.  
Tests won't be able to ass though, because the dependency does not contain our new classes yet.

&lt;Add whatever you want to extend Lombok with.&gt;

Build the Lombok-extension out of our project:  
``mvn clean package -Dmaven.test.skip=true``

Rename *.class files to *.SCL.lombok:  
``rnm target\hello-lombok-1.0-SNAPSHOT.jar .class .SCL.lombok``

(At this point the Lombok-extension related files in your project could not *NOT* compile anymore, 
because Idea does not recognize the .SCL.lombok suffix in the Lombok dependency.)

Merge the very original Lombok JAR with our new creation into a new archive:  
``mrg "hello-lombok-1.0-SNAPSHOT~.jar" lombok-1.16.18.jar hello-lombok-1.0-SNAPSHOT.jar``  

Install the new Lombok:  
``mvn install:install-file 
-Dfile="hello-lombok-1.0-SNAPSHOT.jar" 
-DgroupId=net.jzaruba 
-DartifactId=lombok 
-Dversion=1.16.18 
-Dpackaging=jar``

Run the tests:  
``mvn test``

One day this will be a bit simpler maybe:  
https://stackoverflow.com/questions/35550460/writing-custom-lombok-annotation-handlers
