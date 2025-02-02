http-form-data-demo
-------------------

Demonstrates and validates the usage of `HttpFormData`.

The server-side class receiving requests is [`PartController`](./src/main/java/com/github/fwi/httpformdatademo/PartController.java).

There are two client-side test-classes sending requests, both are very similar.
Both show `form-data` usage with the vanilla JDK `HttpClient` and Spring's `RestClient`.  
The first one [`PartOneTest`](./src/test/java/com/github/fwi/httpformdatademo/PartOneTest.java)
uses a slightly less complicated setup as [`PartTwoTest`](./src/test/java/com/github/fwi/httpformdatademo/PartTwoTest.java).

```bash

# Ensure Java 21 or higher is available
# Ensure http-form-data package is available, in lib-project directory http-form-data run:
mvn clean install

cd http-form-data-demo
mvn -U clean test

# Run just one test

mvn test -Dtest=PartOneTest

# Startup using test-resources

mvn spring-boot:run -Dspring-boot.run.profiles=test -Dspring-boot.run.additional-classpath-elements=src/test/resources

# In separate shell, test server using Curl.

curl -vs http://localhost:8080/
curl -vs http://localhost:8081/actuator/health | jq .

curl -vs http://demo:omed@localhost:8080/me
curl -vs http://demo:omed@localhost:8080/parts/1 -F textPartOne="Hello, world!" -F filePartOne=@README.md

```
