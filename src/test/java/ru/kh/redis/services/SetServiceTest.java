package ru.kh.redis.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kh.redis.dto.setsDto.SetHgetDto;
import ru.kh.redis.dto.setsDto.SetHsetDto;
import ru.kh.redis.utils.Consts;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SetServiceTest {

    @Autowired
    SetService setService;

    SetHsetDto setHsetDto = new SetHsetDto("key", new HashMap<>() {{
        put("keyField1", "value1");
        put("keyField2", "value2");
    }});

    SetHgetDto setHgetDto = new SetHgetDto("key", "keyField1");

    final int DEFAULT_SER_SIZE = 2;

    @Test
    void testSetEntryValuesFieldsWhenSetCreating() {
        String result = setService.setEntryValuesFields(setHsetDto);
        assertEquals(String.valueOf(DEFAULT_SER_SIZE), result);
    }

    @Test
    void testSetEntryValuesFieldsWhenSetExist() {
        setService.setEntryValuesFields(setHsetDto);
        setHsetDto.getFieldsValuesEntry().put("new key", "value");
        String result = setService.setEntryValuesFields(setHsetDto);
        assertEquals(String.valueOf(DEFAULT_SER_SIZE + 1), result);
    }

    @Test
    void testGetFieldByKeySetWhenExist() {
        setService.setEntryValuesFields(setHsetDto);
        String result = setService.getFieldByKeySet(setHgetDto);
        assertEquals("value1", result);
    }

    @Test
    void testGetFieldByKeySetWhenNotExist() {
        String result = setService.getFieldByKeySet(setHgetDto);
        assertEquals(Consts.ERROR_MESSAGE_KEY_NOT_FOUND, result);
    }
}