# Boku Assignment

##How to build the project
 go to the root directory where the build.gradle file is located and
execute below command

./gradlew build or you can just run in your IDE default

## How to run the application
-Go to root directory and build/libs folder where you can find the war file.
-Copy that file to tomcat webapps directory
-Start the tomcat and application will be up and running in the 1337 port

## How to send request to rest api
-curl -X POST -d "1" "http://localhost:1337/assignment-0.0.1-SNAPSHOT/boku/accumulator/process_requests"

## Used Versions of library and Tools
Tomcat : Use 10 or above
Java Vesrion: 17