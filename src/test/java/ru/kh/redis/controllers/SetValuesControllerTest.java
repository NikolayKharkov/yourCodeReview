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
import ru.kh.redis.services.SetService;
import ru.kh.redis.dto.setsDto.SetHgetDto;
import ru.kh.redis.dto.setsDto.SetHsetDto;
import ru.kh.redis.utils.Consts;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(value = SetValuesController.class)
class SetValuesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SetService setService;

    final String HSET = "/hset";

    final String HGET = "/hget";

    final String DEFAULT_SET_SIZE = "2";

    String jsonExampleHset = "{\"key\":\"test_key\"," +
            "\"fieldsValuesEntry\":{\"key1\":\"value\",\"key2\":\"value\"}}";

    String jsonExampleHget = "{\"key\":\"test_key\", \"keySet\":\"key1\"}";

    @Test
    void testHsetWhenSetNotExist() throws Exception {
        when(setService.setEntryValuesFields(Mockito.any(SetHsetDto.class)))
                .thenReturn(DEFAULT_SET_SIZE);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(HSET)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleHset)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(DEFAULT_SET_SIZE, response.getContentAsString());
    }

    @Test
    void testHsetWhenSetToStringEntityThenErrorType() throws Exception {
        when(setService.setEntryValuesFields(Mockito.any(SetHsetDto.class)))
                .thenReturn(Consts.ERROR_MESSAGE_WRONG_TYPE);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(HSET)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleHset)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(Consts.ERROR_MESSAGE_WRONG_TYPE, response.getContentAsString());
    }

    @Test
    void testHgetWhenSetIsExist() throws Exception {
        String expectedValue = "value1";
        when(setService.getFieldByKeySet(Mockito.any(SetHgetDto.class)))
                .thenReturn(expectedValue);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(HGET)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleHget)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedValue, response.getContentAsString());
    }

    @Test
    void testHgetWhenSetNotExist() throws Exception {
        when(setService.getFieldByKeySet(Mockito.any(SetHgetDto.class)))
                .thenReturn(Consts.ERROR_MESSAGE_KEY_NOT_FOUND);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(HGET)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleHget)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(Consts.ERROR_MESSAGE_KEY_NOT_FOUND, response.getContentAsString());
    }
}