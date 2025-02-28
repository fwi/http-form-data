package com.github.fwi.httpformdatademo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import com.github.fwi.httpformdata.FormDataPartFile;
import com.github.fwi.httpformdata.FormDataPartString;
import com.github.fwi.httpformdata.HttpFormData;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "management.server.port=0" })
@Slf4j
@ActiveProfiles("test")
class PartOneTest {

    @LocalServerPort
    int webServerPort;

    HttpClient buidlHttpClient() {
        return HttpClient.newBuilder()
            .version(Version.HTTP_2) // use version HTTP_1_1 for better connection handling in some cases
            .connectTimeout(Duration.ofSeconds(5L))
            .build();
    }

    @Test
    @SneakyThrows
    void httpClient() {

        var client = buidlHttpClient();

        HttpResponse<String> response;

        try (var formData = new HttpFormData()) {
            formData.addPart(new FormDataPartString("textPartOne", "Hello, world!"));
            formData.addPart(new FormDataPartFile("filePartOne", "part.dat", Paths.get("README.md")));
            
            var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + webServerPort + "/parts/1"))
                .header("Authorization", HttpUtils.basicAuth("demo", "omed"))
                .header("Content-Type", formData.getContentType())
                .POST(HttpRequest.BodyPublishers.ofByteArrays(formData))
                .timeout(Duration.ofSeconds(10L))
                .build();
            
            response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        }
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void restClient() {  // NOSONAR

        var requestFactory = new JdkClientHttpRequestFactory(buidlHttpClient());
        requestFactory.setReadTimeout(Duration.ofSeconds(10L));
        
        var client = RestClient.builder()
            .baseUrl("http://localhost:" + webServerPort)
            .requestFactory(requestFactory)
            .build();
        
        var vmap = new LinkedMultiValueMap<String, Object>();
        vmap.add("textPartOne", "Hello, world!");
        vmap.add("filePartOne", new PathResource(Paths.get("README.md")));
        
        client.post()
            .uri("/parts/1")
            .header("Authorization", HttpUtils.basicAuth("demo", "omed"))
            .body(vmap)
            // sadly no option to set read-timeout per request
            .retrieve().body(String.class);
    }

}
