package com.github.fwi.httpformdata;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FormDataPartString extends FormDataPart {
    
    private final String content;
    private final String contenType;
    
    public FormDataPartString(String fieldName, String content) {
        this(fieldName, content, PARAM_TYPE_TEXT_PLAIN_UTF8);
    }

    public FormDataPartString(String fieldName, String content, String contentType) {
        super(fieldName);
        this.content = content;
        this.contenType = contentType;
    }

    @Override
    public String getParamType() {
        return contenType;
    }

    @Override
    public byte[] getContent() {
        return content.getBytes(UTF_8);
    }

}
