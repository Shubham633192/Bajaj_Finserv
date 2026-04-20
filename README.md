Name: Shubham Mishra
Reg No: REG12347

Description: This application developed in Spring Boot is meant for the Bajaj Finserv Health qualifier.

When the application executes,

* First, a POST call is made to create a webhook.
* The SQL query to be formed is generated based on the problem statement.
* Finally, the SQL query is passed to the URL via the token provided.

The application uses CommandLineRunner and therefore does not require any manual trigger of the API.

To run the application,

1. Build the application using Maven.
2. Execute the JAR file by using:
   java -jar target/demo-0.0.1-SNAPSHOT.jar
