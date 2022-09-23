package ru.kh.redis.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kh.redis.models.Key;
import ru.kh.redis.dto.listsDto.ListEntityDto;
import ru.kh.redis.dto.listsDto.ListLGetDto;
import ru.kh.redis.dto.listsDto.ListLindexDto;
import ru.kh.redis.dto.listsDto.ListLsetDto;
import ru.kh.redis.utils.Consts;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ListServiceTest {

    @Autowired
    ListService listService;

    List<String> values = new ArrayList<>(List.of("value1", "value2"));
    ListEntityDto listEntityDto = new ListEntityDto("key", values);

    @Test
    public void testPushValuesWhenNotExist() {
        String result = listService.pushValuesOrCreate(listEntityDto);
        assertEquals(result, String.valueOf(values.size()));
    }

    @Test
    public void testPushValuesWhenExist() {
        listService.pushValuesOrCreate(listEntityDto);
        String result = listService.pushValuesOrCreate(listEntityDto);
        assertEquals(String.valueOf(values.size()), result);
    }

    @Test
    public void testSize() {
        listService.pushValuesOrCreate(listEntityDto);
        String result = listService.getListSize(new Key("key"));
        assertEquals(String.valueOf(values.size()), result);
    }

    @Test
    public void testGetElementByIndex() {
        listService.pushValuesOrCreate(listEntityDto);
        ListLindexDto listLindexDto = new ListLindexDto("key", 1);
        String result = listService.getElementFromListByKey(listLindexDto);
        assertEquals("value2", result);
    }

    @Test
    public void testReplaceElementByIndex() {
        listService.pushValuesOrCreate(listEntityDto);
        ListLsetDto listLsetDto = new ListLsetDto("key", "New value", 0);
        listService.replaceValueByIndex(listLsetDto);
        ListLindexDto listLindexDto = new ListLindexDto("key", 0);
        String result = listService.getElementFromListByKey(listLindexDto);
        assertEquals("New value", result);
    }

    @Test
    public void testGetElementsByStartAndFinishIndices() {
        List<String> values = new ArrayList<>(
                List.of("I", "think", "therefore", "I", "am", "sayeth", "Rene", "Decartes"));
        ListEntityDto listEntityDto = new ListEntityDto("key", values);
        listService.pushValuesOrCreate(listEntityDto);
        ListLGetDto listLGetDto = new ListLGetDto("key", -4, -2);
        String result = listService.getElementsByStartAndFinishIndices(listLGetDto);
        String expected = "1) I\n" +
                "2) am\n" +
                "3) sayeth\n";
        assertEquals(expected, result);
    }

    @Test
    public void testGetElementsByStartAndFinishIndicesAndExpectIndexBoundException() {
        List<String> values = new ArrayList<>(
                List.of("I", "think", "therefore", "I", "am", "sayeth", "Rene", "Decartes"));
        ListEntityDto listEntityDto = new ListEntityDto("key", values);
        listService.pushValuesOrCreate(listEntityDto);
        ListLGetDto listLGetDto = new ListLGetDto("key", 0, 78787);
        String result = listService.getElementsByStartAndFinishIndices(listLGetDto);
        assertEquals(Consts.ERROR_MESSAGE_INDEX_BOUND_OF_ARRAY, result);
    }

}