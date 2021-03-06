### How to make this work: ###
 0) rename files in the original JAR
 0) use the updated JAR as dependency
 0) do the coding
 0) build the extension
 0) rename the files in it
 0) merge the extension with the original
 0) install the updated library
 0) run your tests

### Let's do it ###

In new Lombok the bytecode is delivered in files with *.SCL.lombok suffix, 
instead of the usual *.class. 
This also means you can't depend on classes obscured that way, which further complicates 
extending Lombok.

Rename *.SCL.lombok files in the JAR to *.class:
```
rnm lombok-1.16.18.jar *.lombok .
rnm lombok-1.16.18~.jar *.SCL .class
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
Tests won't be able to pass though, because the dependency does not contain our new classes yet.

&lt;Add whatever you want to extend Lombok with.&gt;

Build the Lombok-extension out of our project:  
``mvn clean package -Dmaven.test.skip=true``

Rename *.class files to *.SCL.lombok:  
``rnm target\hello-lombok-1.0-SNAPSHOT.jar lombok\*.class .SCL.lombok``  
TODO: Files directly under/in the 'lombok' package need to be excluded from renaming!

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
 - https://stackoverflow.com/questions/35550460/writing-custom-lombok-annotation-handlers
 - https://github.com/rzwitserloot/lombok/pull/1296
 