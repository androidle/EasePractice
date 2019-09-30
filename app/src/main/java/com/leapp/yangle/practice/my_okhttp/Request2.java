package com.leapp.yangle.practice.my_okhttp;

import java.util.HashMap;
import java.util.Map;

public class Request2 {

    public static final String GET = "GET";
    public static final String POST = "POST";

    private String url;
    private String method = GET;
    private Map<String, String> headers = new HashMap<>();
    private RequestBody2 requestBody2;

    public Request2() {
        this(new Builder());
    }

    public Request2(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.requestBody2 = builder.requestBody2;
    }

    public static final class Builder{

        private String url;
        private String method = GET;
        private Map<String, String> headers = new HashMap<>();
        private RequestBody2 requestBody2;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder get() {
            this.method = GET;
            return this;
        }

        public Builder post(RequestBody2 requestBody2) {
            this.method = POST;
            this.requestBody2 = requestBody2;
            return this;
        }

        public Builder addRequstHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Request2 build() {
            return new Request2(this);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public RequestBody2 requestBody() {
        return requestBody2;
    }
}
