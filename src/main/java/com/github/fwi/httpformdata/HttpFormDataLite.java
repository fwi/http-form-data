package com.github.fwi.httpformdata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Create form-data suitable for usage with {@link java.net.http.HttpClient}.
 * Use {@link HttpFormData} when parts are added that use the {@link AutoCloseable} interface
 * (e.g. when data is not already in memory like with {@link FormDataPartFile}).
 */
public class HttpFormDataLite implements Iterable<byte[]> {

    private final String boundary = UUID.randomUUID().toString();
    private final List<FormDataPart> parts = new ArrayList<>();

    /**
     * Add parts only BEFORE {@link #iterator()} is called.
     * <p>
     * For {@link AutoCloseable} parts use {@link HttpFormData}
     * which has proper support for closeable parts.
     */
    public void addPart(FormDataPart formPart) {
        parts.add(formPart);
    }

    /**
     * Current list of parts (the actual list, not a copy, so be careful).
     */
    public List<FormDataPart> getParts() {
        return parts;
    }

    /**
     * Boundary used to separate form-data parts.
     */
    public String getBoundary() {
        return boundary;
    }

    /**
     * Use this method for the value of the header "Content-Type"
     */
    public String getContentType() {
        return "multipart/form-data; boundary=" + getBoundary();
    }

    /**
     * This method allows for the usage of this class as parameter for {@link java.net.http.HttpRequest.BodyPublisher}.ofByteArrays
     * <p>
     * Parts are reset before creating a new {@link FormDataPartsIterator}.
     */
    @Override
    public Iterator<byte[]> iterator() {
        parts.forEach(FormDataPart::reset);
        return new FormDataPartsIterator(parts, boundary);
    }

}
