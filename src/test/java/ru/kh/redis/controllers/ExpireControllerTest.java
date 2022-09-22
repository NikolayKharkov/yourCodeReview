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
import ru.kh.redis.Models.Key;
import ru.kh.redis.Services.ExpireService;
import ru.kh.redis.dto.keysDto.KeyExpireDto;

@WebMvcTest(value = ExpireController.class)
class ExpireControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ExpireService expireService;

    final String TTL = "/ttl";

    final String EXPIRE = "/expire";

    final long KEY_NOT_EXPIRED = -1L;

    final long ERROR_KEY_NOT_FOUND = -2L;

    final int EXPIRE_WHEN_KEY_NOT_FOUND = 0;

    final int EXPIRE_WHEN_KEY_FOUND = 1;

    String jsonExampleTtl = "{\"key\":\"test_key\",\"seconds\":5}";

    @Test
    public void testTtlWhenKeyNotExist() throws Exception {
        Mockito.when(expireService.getKeyTTL(Mockito.any(Key.class)))
                .thenReturn(ERROR_KEY_NOT_FOUND);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(TTL)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleTtl)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertEquals(String.valueOf(ERROR_KEY_NOT_FOUND), response.getContentAsString());
    }

    @Test
    public void testTtlWhenKeyExistButNotExpired() throws Exception {
        Mockito.when(expireService.getKeyTTL(Mockito.any(Key.class)))
                .thenReturn(KEY_NOT_EXPIRED);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(TTL)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleTtl)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(String.valueOf(KEY_NOT_EXPIRED), response.getContentAsString());
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
        Assertions.assertEquals("key-Key can't be empty;key-Key can't be null;",
                response.getContentAsString());
    }

    @Test
    public void testTtlWhenInputJsonWithEmptyKey() throws Exception {
        String setJsonWithKeyNull = "{\"key\":\"\",\"seconds\":4}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(TTL)
                .accept(MediaType.APPLICATION_JSON)
                .content(setJsonWithKeyNull)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertEquals("key-Key can't be empty;", response.getContentAsString());
    }

    @Test
    public void testExpireWhenKeyNotExist() throws Exception {
        Mockito.when(expireService.expireKey(Mockito.any(KeyExpireDto.class)))
                .thenReturn(EXPIRE_WHEN_KEY_NOT_FOUND);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(EXPIRE)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleTtl)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertEquals(String.valueOf(EXPIRE_WHEN_KEY_NOT_FOUND), response.getContentAsString());
    }

    @Test
    public void testExpireWhenKeyExist() throws Exception {
        Mockito.when(expireService.expireKey(Mockito.any(KeyExpireDto.class)))
                .thenReturn(EXPIRE_WHEN_KEY_FOUND);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(EXPIRE)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleTtl)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(String.valueOf(EXPIRE_WHEN_KEY_FOUND), response.getContentAsString());
    }
}