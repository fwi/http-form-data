package com.github.fwi.httpformdata;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class HttpFormDataTest {
    
    final byte[] testingBytes = "testing123".getBytes(UTF_8);

    @Test
    @SneakyThrows
    void httpFormData() {
        
        ByteArrayOutputStream bout;
        String boundary;
        try (var f = new HttpFormData()) {
            f.addPart(new FormDataPartString("name1", "hello"));
            f.addPart(new FormDataPartString("name2", "world"));
            f.addPart(new FormDataPartBytes("name3", "test.txt", testingBytes));
            f.addPart(new FormDataPartFile("name4", "form-data-part.dat", Paths.get("src/test/resources/form-data-part.txt"), 3));

            bout = new ByteArrayOutputStream();
            f.iterator().forEachRemaining(b -> write(bout, b));
            boundary = f.getBoundary();
        }
        var actual = new String(bout.toByteArray(), UTF_8);
        log.debug("Body:\n{}", actual);
        actual = actual.replace(boundary, "boundary");
        
        var expected = Files.readString(Paths.get("src/test/resources/form-data-test.txt"));
        // fix line-endings on different OS, need CRLF
        expected = expected.replace("\r", "");
        expected = expected.replace("\n", FormDataPartsIterator.CRLF);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    void write(ByteArrayOutputStream bout, byte[] b) {
        try {
            bout.write(b);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
