package com.github.fwi.httpformdata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Used by {@link HttpFormData} as body-publisher.
 */
public class FormDataPartsIterator implements Iterator<byte[]> {

    public static final String CRLF = "\r\n";

    /* States */
    private static final int PREAMBLE = 10;
    private static final int CONTENT = 20;
    private static final int POSTAMBLE = 30;
    private static final int CLOSURE = 40;

    private final List<FormDataPart> parts;
    private final String boundary;

    private int partIndex;
    private int state = PREAMBLE;

    public FormDataPartsIterator(List<FormDataPart> parts, String boundary) {
        this.parts = parts;
        this.boundary = boundary;
    }

    @Override
    public boolean hasNext() {
        // see use of "closure" in "next" method.
        return partIndex <= parts.size();
    }

    @Override
    public byte[] next() {

        /*
         * By virtue of returning each step as separate (small) byte-chunks,
         * creating/copying large byte-arrays is prevented which in turn can safe memory, time and cpu-usage.
         */

        if (state == PREAMBLE) {
            state = CONTENT;
            var part = parts.get(partIndex);
            var bout = new ByteArrayOutputStream();
            write(bout, "--" + boundary + CRLF);
            write(bout, "Content-Disposition: " + part.getParamDisposition() + CRLF);
            write(bout, "Content-Type: " + part.getParamType() + CRLF);
            write(bout, CRLF);
            return bout.toByteArray();
        }
        
        if (state == CONTENT) {
            var part = parts.get(partIndex);
            var content = part.getContent();
            if (!part.moreContent() || content == null || content.length < 1) {
                state = POSTAMBLE;
            }
            if (content != null && content.length > 0) {
                return content;
            }
            // instead of returning an empty array, continue to returning the postamble
        }
        
        if (state == POSTAMBLE) {
            partIndex++;
            if (partIndex == parts.size()) {
                state = CLOSURE;
            } else {
                state = PREAMBLE;
            }
            return CRLF.getBytes(UTF_8);
        }
        
        if (state == CLOSURE && partIndex == parts.size()) {
            partIndex++;
            return ("--" + boundary + "--" + CRLF).getBytes(UTF_8);
        }
        
        throw new NoSuchElementException("No more form-data parts available.");
    }

    private void write(ByteArrayOutputStream bout, String s) {
        write(bout, s.getBytes(UTF_8));
    }

    private void write(ByteArrayOutputStream bout, byte[] data) {
        try {
            bout.write(data);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write to byte-array stream.", e);
        }
    }

}
