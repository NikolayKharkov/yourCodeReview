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
import ru.kh.redis.models.Key;
import ru.kh.redis.models.entities.StringEntity;
import ru.kh.redis.services.SingleStringsService;
import ru.kh.redis.utils.Consts;

@WebMvcTest(value = StringValuesController.class)
class StringValuesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SingleStringsService singleStringsService;

    final String OK = "OK";

    final String SET = "/set";

    final String GET = "/get";

    final String defaultValue = "defaultValue";

    String exampleSetJson = "{\"key\":\"test_key\",\"value\":\"test_value\"}";

    @Test
    public void testSetRequestWhenNewCreate() throws Exception {
        Mockito.when(singleStringsService.setValue(Mockito.any(Key.class), Mockito.any(StringEntity.class)))
                .thenReturn("OK");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SET)
                .accept(MediaType.APPLICATION_JSON)
                .content(exampleSetJson)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(OK, response.getContentAsString());
    }

    @Test
    public void testSetWhenInputJsonWithNullKey() throws Exception {
        String setJsonWithKeyNull = "{\"key\":null,\"value\":\"test_value\"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SET)
                .accept(MediaType.APPLICATION_JSON)
                .content(setJsonWithKeyNull)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertEquals("key-Key can't be empty;key-Key can't be null;", response.getContentAsString());
    }

    @Test
    public void testSetWhenInputJsonWithEmptyKey() throws Exception {
        String setJsonWithEmptyKey = "{\"key\":\"\",\"value\":\"test_value\"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SET)
                .accept(MediaType.APPLICATION_JSON)
                .content(setJsonWithEmptyKey)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertEquals("key-Key can't be empty;", response.getContentAsString());
    }

    @Test
    public void testSetWhenInputJsonWithNullValue() throws Exception {
        String setJsonWithNullValue = "{\"key\":\"test_key\",\"value\":null}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SET)
                .accept(MediaType.APPLICATION_JSON)
                .content(setJsonWithNullValue)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertEquals("value-Value can't be empty;value-Value can't be null;",
                response.getContentAsString());
    }

    @Test
    public void testSetWhenInputJsonWithEmptyValue() throws Exception {
        String setJsonWithEmptyValue = "{\"key\":\"test_key\",\"value\":\"\"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SET)
                .accept(MediaType.APPLICATION_JSON)
                .content(setJsonWithEmptyValue)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertEquals("value-Value can't be empty;", response.getContentAsString());
    }

    @Test
    public void testSetWhenStoredEntityNotStringType() throws Exception {
        Mockito.when(singleStringsService.setValue(Mockito.any(Key.class), Mockito.any(StringEntity.class)))
                .thenReturn(Consts.ERROR_MESSAGE_WRONG_TYPE);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SET)
                .accept(MediaType.APPLICATION_JSON)
                .content(exampleSetJson)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertEquals(Consts.ERROR_MESSAGE_WRONG_TYPE, response.getContentAsString());
    }

    @Test
    public void testGetWhenEntityIsStored() throws Exception {
        Mockito.when(singleStringsService.getStringValueByKey(Mockito.any(Key.class)))
                .thenReturn(defaultValue);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(GET)
                .accept(MediaType.APPLICATION_JSON)
                .content(exampleSetJson)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(defaultValue, response.getContentAsString());
    }

    @Test
    public void testGetWhenKeyIsNotFound() throws Exception {
        Mockito.when(singleStringsService.getStringValueByKey(Mockito.any(Key.class)))
                .thenReturn(Consts.ERROR_MESSAGE_KEY_NOT_FOUND);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(GET)
                .accept(MediaType.APPLICATION_JSON)
                .content(exampleSetJson)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertEquals(Consts.ERROR_MESSAGE_KEY_NOT_FOUND, response.getContentAsString());
    }

    @Test
    public void testGetWhenEntityNotCorrectType() throws Exception {
        Mockito.when(singleStringsService.getStringValueByKey(Mockito.any(Key.class)))
                .thenReturn(Consts.ERROR_MESSAGE_WRONG_TYPE);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(GET)
                .accept(MediaType.APPLICATION_JSON)
                .content(exampleSetJson)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertEquals(Consts.ERROR_MESSAGE_WRONG_TYPE, response.getContentAsString());
    }
}