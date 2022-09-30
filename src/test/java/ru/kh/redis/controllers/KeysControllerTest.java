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
import ru.kh.redis.services.KeysService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(value = KeysController.class)
class KeysControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private KeysService keysService;

    final int NUM_DELETE_KEYS = 3;

    final String NUM_DELETE_KEYS_TEXT = "(integer) 3";

    final String DEL = "/del";

    final String KEYS = "/keys";

    String jsonExampleDel = "{\"keys\": [\"key1\", \"key2\", \"key3\"]}";

    String jsonExampleKeys = "{\"pattern\": \"*\"}";

    String resultOfKeyPattern = "1) key1" + "\n" + "2) key2";

    @Test
    public void testDel() throws Exception {
        String[] array = new String[0];
        when(keysService.delKeys(Mockito.any(array.getClass())))
                .thenReturn(NUM_DELETE_KEYS);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(DEL)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleDel)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(NUM_DELETE_KEYS_TEXT, response.getContentAsString());
    }

    @Test
    public void testKeys() throws Exception {
        when(keysService.getKeys(Mockito.any(String.class)))
                .thenReturn(resultOfKeyPattern);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(KEYS)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonExampleKeys)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(resultOfKeyPattern, response.getContentAsString());
    }

}