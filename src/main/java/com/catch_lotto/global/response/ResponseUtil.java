package com.catch_lotto.global.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeJson(HttpServletResponse response, ApiResponse<?> apiResponse) throws IOException {
        response.setStatus(apiResponse.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String result = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(result);
    }
}
