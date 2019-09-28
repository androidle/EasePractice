package com.leapp.yangle.practice.my_okhttp;

import java.util.HashMap;
import java.util.Map;

public class Request2 {

    public static final String GET = "GET";
    public static final String POST = "POST";

    private String url;
    private String method = GET;
    private Map<String, String> headers = new HashMap<>();

    public Request2() {
        this(new Builder());
    }

    public Request2(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
    }

    public static final class Builder{

        private String url;
        private String method = GET;
        private Map<String, String> headers = new HashMap<>();

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder get() {
            this.method = GET;
            return this;
        }

        public Builder post() {
            this.url = POST;
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
}
