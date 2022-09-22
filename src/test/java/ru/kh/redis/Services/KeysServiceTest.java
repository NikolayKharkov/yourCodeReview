package ru.kh.redis.Services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kh.redis.Models.Key;
import ru.kh.redis.Models.entities.StringEntity;
import ru.kh.redis.Repositories.CacheRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KeysServiceTest {

    @Autowired
    KeysService keysService;

    @Autowired
    SingleStringsService stringsService;

    @Autowired
    CacheRepository cacheRepository;

    @Test
    public void testDel() {
        String[] keys = {"key1", "key2", "key3"};
        for (String key : keys) {
            stringsService.setValue(new Key(key), new StringEntity("value"));
        }
        int result = keysService.delKeys(new String[]{"key1", "key2", "key3", "NotExistKey"});
        assertEquals(result, 3);
    }

    @Test
    public void testKeysWhenAllPattern() {
        String pattern = "*";
        List<String> keys = List.of("key1", "key2", "key3");
        for (String key : keys) {
            stringsService.setValue(new Key(key), new StringEntity("value"));
        }
        List<String> result = cacheRepository.getKeysByPattern(pattern).stream().sorted().toList();
        assertEquals(keys, result);
    }

    @Test
    public void testKeysWhenAskSign() {
        String pattern = "k?";
        List<String> keys = List.of("key1", "key2", "ke");
        List<String> expected = List.of("ke");
        for (String key : keys) {
            stringsService.setValue(new Key(key), new StringEntity("value"));
        }
        List<String> result = cacheRepository.getKeysByPattern(pattern);
        assertEquals(expected, result);
    }

    @Test
    public void testKeysWhenStarSign() {
        String pattern = "k*2";
        List<String> keys = List.of("key1", "key2", "key3");
        List<String> expected = List.of("key2");
        for (String key : keys) {
            stringsService.setValue(new Key(key), new StringEntity("value"));
        }
        List<String> result = cacheRepository.getKeysByPattern(pattern);
        assertEquals(expected, result);
    }

    @Test
    public void testKeysWhenMatchersLetters() {
        String pattern = "h[ae]llo";
        List<String> keys = List.of("hello", "hallo", "hillo");
        List<String> expected = List.of("hallo", "hello");
        for (String key : keys) {
            stringsService.setValue(new Key(key), new StringEntity("value"));
        }
        List<String> result = cacheRepository.getKeysByPattern(pattern);
        assertEquals(expected, result);
    }

    @Test
    public void testKeysWhenNoMatchersLetters() {
        String pattern = "h[^e]llo";
        List<String> keys = List.of("hello", "hallo", "hbllo");
        List<String> expected = List.of("hallo", "hbllo");
        for (String key : keys) {
            stringsService.setValue(new Key(key), new StringEntity("value"));
        }
        List<String> result = cacheRepository.getKeysByPattern(pattern);
        assertEquals(expected, result);
    }

    @Test
    public void testKeysWhenMatchersLettersSpecific() {
        String pattern = "h[a-b]llo";
        List<String> keys = List.of("hello", "hallo", "hbllo");
        List<String> expected = List.of("hallo", "hbllo");
        for (String key : keys) {
            stringsService.setValue(new Key(key), new StringEntity("value"));
        }
        List<String> result = cacheRepository.getKeysByPattern(pattern);
        assertEquals(expected, result);
    }
}