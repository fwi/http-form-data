package com.github.fwi.httpformdata;

import java.io.Closeable;
import java.io.IOException;

/**
 * A {@link HttpFormDataLite} with support for (auto) closeable parts.
 * Use this class with a try-with-resources construction
 * to ensure any {@link AutoCloseable} parts are properly closed.
 */
public class HttpFormData extends HttpFormDataLite implements Closeable {

    /**
     * Closes all {@link AutoCloseable} {@link FormDataPart}s.
     * @throws IOException Contains a message with all {@link IOException}s that might have occurred.
     */
    public void close() throws IOException {

        var closeFailed = false;
        var closeExceptions = new StringBuilder("HttpFormData close exceptions - ");
        for (var part : getParts()) {
            if (part instanceof AutoCloseable parta) {
                try {
                    parta.close();
                } catch (Exception e) {
                    if (closeFailed) {
                        closeExceptions.append("\n");
                    } else {
                        closeFailed = true;
                    }
                    closeExceptions.append(e.toString());
                }
            }
        }
        if (closeFailed) {
            throw new IOException(closeExceptions.toString());
        }
    }

}
