package com.bs.mycareer.utils;

import com.bs.mycareer.Common.exceptions.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.bs.mycareer.Common.exceptions.ResponseCode.*;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T readValue(HttpServletRequest request, Class<T> type) {
        try {
            // InputStream을 사용하여 request body를 읽어옴
            InputStream inputStream = request.getInputStream();

            /// URL-decode 수행
            String jsonBody = URLDecoder.decode(new String(inputStream.readAllBytes()), StandardCharsets.UTF_8);

            // 로깅 추가
            System.out.println("Received JSON Body: " + jsonBody);

            return mapper.readValue(new ByteArrayInputStream(jsonBody.getBytes()), type);
        } catch (IOException e) {
            System.out.println("e.message = " + e);
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }
    }
    public static void writeValue(OutputStream stream, Object value) {
        try {
            mapper.writeValue(stream, value);
        } catch (IOException e) {
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }
    }
}


//    public static void writeValue(HttpServletResponse response, Object value) {
//        try {
//            mapper.writeValue(response.getOutputStream(), value);
//        } catch (IOException e) {
//            throw new CommonException(e.getMessage(), e);
//        }

//    }


