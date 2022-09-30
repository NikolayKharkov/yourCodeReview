package ru.kh.redis.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kh.redis.dto.keysDto.KeyDto;
import ru.kh.redis.models.Key;
import ru.kh.redis.models.entities.StringEntity;
import ru.kh.redis.dto.listsDto.ListEntityDto;
import ru.kh.redis.utils.Consts;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SingleStringsServiceTest {

    @Autowired
    SingleStringsService stringsService;

    @Autowired
    ListService listService;

    Key defaultKey = new Key("key");

    KeyDto keyDto = new KeyDto("key");

    StringEntity defaultStringEntity = new StringEntity("value");

    String defaultKeyValue = "key";

    String defaultValue = "value";

    @Test
    void testSetWhenKeyNotExistAndExpectedOk() {
        String result = stringsService.setValue(defaultKey, defaultStringEntity);
        assertEquals(result, Consts.OK);
    }

    @Test
    void testSetWhenPutTwiceAndValueOverwrite() {
        stringsService.setValue(defaultKey, defaultStringEntity);
        String firstValue = stringsService.getStringValueByKey(keyDto);
        String updatedValue = "value_update";
        stringsService.setValue(defaultKey, new StringEntity(updatedValue));
        String secondValue = stringsService.getStringValueByKey(keyDto);
        assertEquals(firstValue, defaultValue);
        assertEquals(updatedValue, secondValue);
    }

    @Test
    void testGetWhenKeyNotExist() {
        String result = stringsService.getStringValueByKey(keyDto);
        assertEquals(result, Consts.ERROR_MESSAGE_KEY_NOT_FOUND);

    }

    @Test
    void testGetWhenKeyIsExist() {
        stringsService.setValue(defaultKey, defaultStringEntity);
        String result = stringsService.getStringValueByKey(keyDto);
        assertEquals(result, defaultValue);
    }

    @Test
    void testSetWhenKeyNotStringEntityType() {
        ListEntityDto listEntityDto = new ListEntityDto(defaultKeyValue, new ArrayList<>(List.of(defaultValue)));
        listService.pushValuesOrCreate(listEntityDto);
        String result = stringsService.setValue(defaultKey, defaultStringEntity);
        assertEquals(result, Consts.ERROR_MESSAGE_WRONG_TYPE);
    }

    @Test
    void testGetWhenKeyNotStringEntityType() {
        ListEntityDto listEntityDto = new ListEntityDto(defaultKeyValue, new ArrayList<>(List.of(defaultValue)));
        listService.pushValuesOrCreate(listEntityDto);
        String result = stringsService.getStringValueByKey(keyDto);
        assertEquals(result, Consts.ERROR_MESSAGE_WRONG_TYPE);
    }
}