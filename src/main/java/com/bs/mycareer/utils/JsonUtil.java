package com.bs.mycareer.utils;

import com.bs.mycareer.exceptions.CommonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T readValue(HttpServletRequest request, Class<T> type) {
        try {
            return mapper.readValue(request.getInputStream(), type);
        } catch (IOException e) {
            throw new CommonException(e.getMessage(), e);
        }
    }
}

//    public static void writeValue(OutputStream stream, Object value) {
//        try {
//            mapper.writeValue(stream, value);
//        } catch (IOException e) {
//            throw new CommonException(e.getMessage(), e);
//        }
//    }

//    public static void writeValue(HttpServletResponse response, Object value) {
//        try {
//            mapper.writeValue(response.getOutputStream(), value);
//        } catch (IOException e) {
//            throw new CommonException(e.getMessage(), e);
//        }

//    }


