### Smart AI Task Manager
This project demonstrates how to use Ollama and Spring AI 
to generate and prioritize task according to deadlines and 
commitment time needed to finish tasks. 

Ollama is a free, open-source tool that allows users to 
run large  language models (LLMs) locally on their own 
machine.

So, its free of cost and internet for its usage and 
suitable for all the students all over the world.

## Installation
To install this project, you need to have Java 17+, 
SpringBoot 3.3.5, spring-ai-ollama, spring-web, 
spring-boot-starter-data-jpa, My Sql 8.0.4, Maven 3.6+  
Node 20.11.1+, VS Code 1.95.3 installed on your system.

Go to www.ollama.com for downloading ollama.
## Execution
First download the smart-task-manager project which has 
code for backend api.
Open it in IntelliJ/ your favorite editor and run the 
project directly or To start the Spring Boot application, 
run: mvn spring-boot:run via command prompt.
Test it in your browser: http://localhost:8040/, it should 
show white label error. This means backend api is up and 
ready to use
Then, open the smart-time-manager-ui project which has code
for frontend.
Open the folder in VS Code, run npm install in the terminal.
Once all the libraries are downloaded, run npm start to 
start the development server.
Test it in your browser : http://localhost:3000
## Usage
Follow this video link on usage of the project.



























### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.5/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.5/maven-plugin/build-image.html)
* [Ollama](https://docs.spring.io/spring-ai/reference/api/chat/ollama-chat.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.3.5/reference/web/servlet.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

