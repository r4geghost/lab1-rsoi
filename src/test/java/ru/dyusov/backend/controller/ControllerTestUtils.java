package ru.dyusov.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class ControllerTestUtils {
    public static ObjectMapper getMapper() {
        return JsonMapper.builder().build();
    }

    public static MockHttpServletRequestBuilder postJson(String uri, Object body) {
        try {
            String json = getMapper().writeValueAsString(body);
            return post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static MockHttpServletRequestBuilder deleteJson(String uri, int id) {
        try {
            return delete(uri, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MockHttpServletRequestBuilder patchJson(String uri, int id, Object body) {
        try {
            String json = getMapper().writeValueAsString(body);
            return patch(uri, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
