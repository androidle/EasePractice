package com.leapp.yangle.practice.my_okhttp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestBody2 {

    //表单提交
    public static final String TYPE = "application/x-www-form-urlencoded";

    public static final String UTF_8 = "utf-8";

    //请求体集合
    private Map<String, String> formBodys = new HashMap<>();

    public void addBody(String key, String value) {
        try {
            formBodys.put(URLEncoder.encode(key,UTF_8), URLEncoder.encode(value,UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getBody() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : formBodys.entrySet()) {
            // a=xxx&b=xxx
            stringBuffer.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }

        // 删除未尾&
        if (stringBuffer.length() != 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }
}
