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
import ru.kh.redis.services.ListService;
import ru.kh.redis.dto.listsDto.ListEntityDto;
import ru.kh.redis.dto.listsDto.ListLGetDto;
import ru.kh.redis.dto.listsDto.ListLindexDto;
import ru.kh.redis.dto.listsDto.ListLsetDto;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(value = ListValuesController.class)
class ListValuesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ListService listService;

    ListEntityDto listEntityDto = new ListEntityDto("key", new ArrayList<>(List.of("value1", "value2")));

    String DEFAULT_SIZE_LIST = "2";

    String INTEGER = "(integer) ";

    String LPUSH = "/lpush";

    String LSET = "/lset";

    String LGET = "/lget";

    String LINDEX = "/lindex";

    String LLEN = "/llen";

    String jsonExampleLpush = "{\"key\":\"test_key\",\"values\":[\"value1\", \"value2\"]}";

    String jsonExampleLset = "{\"key\":\"test_key\",\"value\":\"value1\",\"index\": 0}";

    String jsonExampleLget = "{\"key\":\"test_key\",\"startIndex\":0,\"finishIndex\": 0}";

    String jsonExampleLindex = "{\"key\":\"test_key\",\"index\":0}";

    String jsonExampleLlen = "{\"key\":\"test_key\"}";

    @Test
    void testLpushWhenNewList() throws Exception {
        Mockito.when(listService.pushValuesOrCreate(Mockito.any(ListEntityDto.class)))
                .thenReturn(DEFAULT_SIZE_LIST);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(LPUSH)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleLpush)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(INTEGER + DEFAULT_SIZE_LIST, response.getContentAsString());
    }

    @Test
    void testLsetWhenListExist() throws Exception {
        Mockito.when(listService.replaceValueByIndex(Mockito.any(ListLsetDto.class)))
                .thenReturn(DEFAULT_SIZE_LIST);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(LSET)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleLset)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(DEFAULT_SIZE_LIST, response.getContentAsString());
    }

    @Test
    void testLgetWhenListExist() throws Exception {
        String values = "1) value1";
        Mockito.when(listService.getElementsByStartAndFinishIndices(Mockito.any(ListLGetDto.class)))
                .thenReturn(values);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(LGET)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleLget)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(values, response.getContentAsString());
    }

    @Test
    void testLindexWhenListExist() throws Exception {
        String value = "value1";
        Mockito.when(listService.getElementFromListByKey(Mockito.any(ListLindexDto.class)))
                .thenReturn(value);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(LINDEX)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleLindex)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(value, response.getContentAsString());
    }

    @Test
    void testLlenWhenListExist() throws Exception {
        Mockito.when(listService.getListSize(Mockito.any(Key.class)))
                .thenReturn(DEFAULT_SIZE_LIST);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(LLEN)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleLlen)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(DEFAULT_SIZE_LIST, response.getContentAsString());
    }
}