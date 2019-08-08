package com.quikrTask.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

public class JsonUtils {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static String toJsonString(Object obj) throws Exception {
        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, obj);
        return writer.toString();
    }

    public static <T> T fromJsonString(Class<T> clazz, String requestJson) throws Exception {
        T t = null;

        InputStream is = new ByteArrayInputStream(requestJson.getBytes("UTF-8"));
        t = objectMapper.readValue(is, clazz);


        return t;
    }
}
