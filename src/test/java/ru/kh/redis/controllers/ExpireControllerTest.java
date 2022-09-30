package ru.kh.redis.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.kh.redis.dto.keysDto.KeyDto;
import ru.kh.redis.models.Key;
import ru.kh.redis.services.ExpireService;
import ru.kh.redis.dto.keysDto.KeyExpireDto;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(value = ExpireController.class)
class ExpireControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ExpireService expireService;

    final String TTL = "/ttl";

    final String EXPIRE = "/expire";

    final String KEY_NOT_EXPIRED = "-1";

    final String ERROR_KEY_NOT_FOUND = "-2";

    final String EXPIRE_WHEN_KEY_NOT_FOUND = "0";

    final String EXPIRE_WHEN_KEY_FOUND = "1";

    String jsonExampleTtl = "{\"key\":\"test_key\",\"seconds\":5}";

    String keyCantBeNullJson = "{\"key\":\"Key can't be null. Actual value: null\"}";

    String keyCantBeEmptyJson = "{\"key\":\"Key can't be empty. Actual value: null\"}";

    @Test
    public void testTtlWhenKeyNotExist() throws Exception {
        when(expireService.getKeyTTL(Mockito.any(KeyDto.class)))
                .thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(TTL)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleTtl)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(ERROR_KEY_NOT_FOUND, response.getContentAsString());
    }

    @Test
    public void testTtlWhenKeyExistButNotExpired() throws Exception {
        when(expireService.getKeyTTL(Mockito.any(KeyDto.class)))
                .thenReturn(KEY_NOT_EXPIRED);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(TTL)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleTtl)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(KEY_NOT_EXPIRED, response.getContentAsString());
    }

    @Test
    public void testTtlWhenInputJsonWithNullKey() throws Exception {
        String setJsonWithKeyNull = "{\"key\":null,\"seconds\":4}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(TTL)
                .accept(MediaType.APPLICATION_JSON)
                .content(setJsonWithKeyNull)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertEquals("[" + keyCantBeNullJson + "," + keyCantBeEmptyJson + "]",
                response.getContentAsString());
    }

    @Test
    public void testTtlWhenInputJsonWithEmptyKey() throws Exception {
        String setJsonWithKeyNull = "{\"key\":\"\",\"seconds\":4}";
        String expected = "{\"key\":\"Key can't be empty. Actual value: \"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(TTL)
                .accept(MediaType.APPLICATION_JSON)
                .content(setJsonWithKeyNull)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("[" + expected + "]", response.getContentAsString());
    }

    @Test
    public void testExpireWhenKeyNotExist() throws Exception {
        when(expireService.expireKey(Mockito.any(KeyExpireDto.class)))
                .thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(EXPIRE)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleTtl)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(EXPIRE_WHEN_KEY_NOT_FOUND, response.getContentAsString());
    }

    @Test
    public void testExpireWhenKeyExist() throws Exception {
        when(expireService.expireKey(Mockito.any(KeyExpireDto.class)))
                .thenReturn(EXPIRE_WHEN_KEY_FOUND);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(EXPIRE)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleTtl)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(EXPIRE_WHEN_KEY_FOUND, response.getContentAsString());
    }
}