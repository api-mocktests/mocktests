package org.api.mocktests.models;

import org.api.mocktests.exceptions.InvalidRequestException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public final class Request {

    private Method method;

    private String url;

    private Header header;

    private Object[] pathParams;

    private MultiValueMap<String, String> params;

    private MediaType contentType;

    private Object body;

    private String content;

    public Request() {
        super();
    }

    public Request method(Method method) {
        this.method = method;
        return this;
    }

    public Request url(String url) {
        this.url = url;
        return this;
    }

    public Request header(String name, TypeHeader typeHeader, String... values) {
        this.header = new Header(name, typeHeader, values);
        return this;
    }

    public Request header(String... values) {
        this.header = new Header(null, null, values);
        return this;
    }

    public Request pathParams(Object... pathParams) {
        this.pathParams = pathParams;
        return this;
    }

    public Request params(MultiValueMap<String, String> params) {
        if(this.params == null)
            this.params = new LinkedMultiValueMap<>();
        this.params.addAll(params);
        return this;
    }

    public Request param(String key, String value) {
        if(params == null)
            params = new LinkedMultiValueMap<>();
        params.add(key, value);
        return this;
    }

    public Request contentType(MediaType contentType) {
        this.contentType = contentType;
        return this;
    }

    public Request body(Object body) {
        this.body = body;
        return this;
    }

    public Request body(String body) {
        this.content = body;
        return this;
    }

    public void verifyMethod() throws InvalidRequestException {
        if(method == null)
            throw new InvalidRequestException("method not nullable");
    }


    public void verifyUrl() throws InvalidRequestException {
        if(url == null)
            throw new InvalidRequestException("endpoint not nullable");
    }

    public Method getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Object[] getPathParams() {
        return pathParams;
    }

    public MultiValueMap<String, String> getParams() {
        return params;
    }

    public Header getHeader() {
        return header;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public Object getBody() {
        return body;
    }

    public String getContent() {
        return content;
    }
}
