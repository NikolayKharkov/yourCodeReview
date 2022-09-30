package ru.kh.redis.controllers;

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
import ru.kh.redis.models.entities.StringEntity;
import ru.kh.redis.services.SingleStringsService;
import ru.kh.redis.utils.Consts;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(value = StringValuesController.class)
class StringValuesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SingleStringsService singleStringsService;

    final String SET = "/set";

    final String GET = "/get";

    final String defaultValue = "defaultValue";

    String exampleSetJson = "{\"key\":\"test_key\",\"value\":\"test_value\"}";

    String keyCantBeNullJson = "{\"key\":\"Key can't be null. Actual value: null\"}";

    String keyCantBeEmptyJson = "{\"key\":\"Key can't be empty. Actual value: null\"}";

    String valueCantBeNullJson = "{\"value\":\"Value can't be null. Actual value: null\"}";

    String valueCantBeEmptyJson = "{\"value\":\"Value can't be empty. Actual value: null\"}";

    @Test
    public void testSetRequestWhenNewCreate() throws Exception {
        when(singleStringsService.setValue(Mockito.any(Key.class), Mockito.any(StringEntity.class)))
                .thenReturn(Consts.OK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SET)
                .accept(MediaType.APPLICATION_JSON)
                .content(exampleSetJson)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(Consts.OK, response.getContentAsString());
    }

    @Test
    public void testSetWhenInputJsonWithNullKey() throws Exception {
        String setJsonWithKeyNull = "{\"key\":null,\"value\":\"test_value\"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SET)
                .accept(MediaType.APPLICATION_JSON)
                .content(setJsonWithKeyNull)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("[" + keyCantBeNullJson + "," + keyCantBeEmptyJson + "]",
        response.getContentAsString());
    }

    @Test
    public void testSetWhenInputJsonWithEmptyKey() throws Exception {
        String setJsonWithEmptyKey = "{\"key\":\"\",\"value\":\"test_value\"}";
        String expected = "{\"key\":\"Key can't be empty. Actual value: \"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SET)
                .accept(MediaType.APPLICATION_JSON)
                .content(setJsonWithEmptyKey)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("[" + expected + "]", response.getContentAsString());
    }

    @Test
    public void testSetWhenInputJsonWithNullValue() throws Exception {
        String setJsonWithNullValue = "{\"key\":\"test_key\",\"value\":null}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SET)
                .accept(MediaType.APPLICATION_JSON)
                .content(setJsonWithNullValue)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("[" + valueCantBeEmptyJson  + "," + valueCantBeNullJson + "]",
                response.getContentAsString());
    }

    @Test
    public void testSetWhenInputJsonWithEmptyValue() throws Exception {
        String setJsonWithEmptyValue = "{\"key\":\"test_key\",\"value\":\"\"}";
        String expected = "{\"value\":\"Value can't be empty. Actual value: \"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(SET)
                .accept(MediaType.APPLICATION_JSON)
                .content(setJsonWithEmptyValue)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("[" + expected + "]", response.getContentAsString());
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
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(Consts.ERROR_MESSAGE_WRONG_TYPE, response.getContentAsString());
    }

    @Test
    public void testGetWhenEntityIsStored() throws Exception {
        when(singleStringsService.getStringValueByKey(Mockito.any(KeyDto.class)))
                .thenReturn(defaultValue);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(GET)
                .accept(MediaType.APPLICATION_JSON)
                .content(exampleSetJson)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(defaultValue, response.getContentAsString());
    }

    @Test
    public void testGetWhenKeyIsNotFound() throws Exception {
        when(singleStringsService.getStringValueByKey(Mockito.any(KeyDto.class)))
                .thenReturn(Consts.ERROR_MESSAGE_KEY_NOT_FOUND);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(GET)
                .accept(MediaType.APPLICATION_JSON)
                .content(exampleSetJson)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(Consts.ERROR_MESSAGE_KEY_NOT_FOUND, response.getContentAsString());
    }

    @Test
    public void testGetWhenEntityNotCorrectType() throws Exception {
        when(singleStringsService.getStringValueByKey(Mockito.any(KeyDto.class)))
                .thenReturn(Consts.ERROR_MESSAGE_WRONG_TYPE);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(GET)
                .accept(MediaType.APPLICATION_JSON)
                .content(exampleSetJson)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(Consts.ERROR_MESSAGE_WRONG_TYPE, response.getContentAsString());
    }
}