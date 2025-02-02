http-form-data
--------------

Posting `multipart/form-data` using the plain `java.net.http.HttpClient` is not trivial.
This library contains the building blocks to post text, data and files 
using one [`HttpFormData`](./src/main/java/com/github/fwi/httpformdata/HttpFormData.java)-instance.

For a full-featured HttpClient wrapper, take a look at [Methanol](https://mizosoft.github.io/methanol/) for example.

The base class [`FormDataPart`](./src/main/java/com/github/fwi/httpformdata/FormDataPart.java),
or any of it's implementations, can be extended to accomodate custom form-data options.
The class [`FormDataPartBytes`](./src/main/java/com/github/fwi/httpformdata/FormDataPartBytes.java)
and [`FormDataPartString`](./src/main/java/com/github/fwi/httpformdata/FormDataPartString.java)
serve as a simple implementation examples while
the class [`FormDataPartFile`](./src/main/java/com/github/fwi/httpformdata/FormDataPartFile.java)
serves as a complex one.  

## Usage

```java

HttpResponse<String> response;

try (var formData = new HttpFormData()) {
    formData.addPart(new FormDataPartString("text-field-name", "Hello, world!"));
    formData.addPart(new FormDataPartFile("file-field-name", "README.md", Paths.get("README.md")));

    var request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost/form"))
        .header("Content-Type", formData.getContentType())
        .POST(HttpRequest.BodyPublishers.ofByteArrays(formData))
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
}

```

## Installation

Clone/copy this project locally, ensure Java 21 or higher is used, then:

    mvn install

## Demo / testing

Included in this project is a demo-project which compares Spring's `RestClient`
with vanilla `java.net.http.HttpClient` usage. At the same time,
Spring Boot's web-server (Tomcat) is used to showcase and validate `form-data` requests.
See the demo project's [README](./http-form-data-demo/README.md) for more information.

## Credits

Inspired by an [answer by ittupelo](https://stackoverflow.com/a/54675316/3080094) on the question posted at
https://stackoverflow.com/questions/46392160/java-9-httpclient-send-a-multipart-form-data-request
