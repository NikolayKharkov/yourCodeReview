package ru.kh.redis.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kh.redis.dto.keysDto.KeyExpireDto;
import ru.kh.redis.models.Key;
import ru.kh.redis.models.entities.StringEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CacheRepositoryTest {

    @Autowired
    private CacheRepository cacheRepository;

    Key key = new Key("test_key");

    StringEntity value = new StringEntity("value");

    @Test
    void testPutAndGetValue() {
        cacheRepository.putValue(key, value);
        StringEntity expectedValue = new StringEntity("value");
        assertEquals(expectedValue.getValue(), cacheRepository.getValue(key).getValue());
    }


    @Test
    void testHasKeyWhenKeyExists() {
        cacheRepository.putValue(key, value);
        assertTrue(cacheRepository.hasKey(key));
    }

    @Test
    void testHasKeyWhenKeyNotExists() {
        assertFalse(cacheRepository.hasKey(key));
    }

    @Test
    void testGetKey() {
        cacheRepository.putValue(key, value);
        assertEquals(key, cacheRepository.getKey(key));
    }

    @Test
    void testRemovesKeys() {
        int expectedCountDeletedKeys = 2;
        Key key1 = new Key("key_1");
        Key key2 = new Key("key_2");
        Key key3 = new Key("key_3");
        cacheRepository.putValue(key1, value);
        cacheRepository.putValue(key2, value);
        cacheRepository.putValue(key3, value);
        String[] keys = {key1.getKeyValue(), key3.getKeyValue(), "NOT_EXIST_KEY"};
        int countDeletedKeys = cacheRepository.removesKeys(keys);
        assertEquals(expectedCountDeletedKeys, countDeletedKeys);
    }

    @Test
    void testGetKeysByAllPattern() {
        String pattern = "*";
        List<String> keys = List.of("key1", "key2", "key3");
        for (String key : keys) {
            cacheRepository.putValue(new Key(key), value);
        }
        List<String> result = cacheRepository.getKeysByPattern(pattern).stream().sorted().toList();
        assertEquals(keys, result);
    }

    @Test
    void testKeysWhenAskSign() {
        String pattern = "k?";
        List<String> keys = List.of("key1", "key2", "ke");
        List<String> expected = List.of("ke");
        for (String key : keys) {
            cacheRepository.putValue(new Key(key), value);
        }
        List<String> result = cacheRepository.getKeysByPattern(pattern);
        assertEquals(expected, result);
    }

    @Test
    void testKeysWhenStarSign() {
        String pattern = "k*2";
        List<String> keys = List.of("key1", "key2", "key3");
        List<String> expected = List.of("key2");
        for (String key : keys) {
            cacheRepository.putValue(new Key(key), value);
        }
        List<String> result = cacheRepository.getKeysByPattern(pattern);
        assertEquals(expected, result);
    }

    @Test
    void testKeysWhenMatchersLetters() {
        String pattern = "h[ae]llo";
        List<String> keys = List.of("hello", "hallo", "hillo");
        List<String> expected = List.of("hallo", "hello");
        for (String key : keys) {
            cacheRepository.putValue(new Key(key), value);
        }
        List<String> result = cacheRepository.getKeysByPattern(pattern);
        assertEquals(expected, result);
    }

    @Test
    void testKeysWhenNoMatchersLetters() {
        String pattern = "h[^e]llo";
        List<String> keys = List.of("hello", "hallo", "hbllo");
        List<String> expected = List.of("hallo", "hbllo");
        for (String key : keys) {
            cacheRepository.putValue(new Key(key), value);
        }
        List<String> result = cacheRepository.getKeysByPattern(pattern);
        assertEquals(expected, result);
    }

    @Test
    void testKeysWhenMatchersLettersSpecific() {
        String pattern = "h[a-b]llo";
        List<String> keys = List.of("hello", "hallo", "hbllo");
        List<String> expected = List.of("hallo", "hbllo");
        for (String key : keys) {
            cacheRepository.putValue(new Key(key), value);
        }
        List<String> result = cacheRepository.getKeysByPattern(pattern);
        assertEquals(expected, result);
    }

    @Test
    void testSetExpireOnKeyWhenKeyExists() {
        cacheRepository.putValue(key, value);
        String result = cacheRepository.setExpireOnKey(new KeyExpireDto(key.getKeyValue(), 1));
        assertEquals(result, "1");
    }

    @Test
    void testSetExpireOnKeyWhenKeyNotExists() {
        String result = cacheRepository.setExpireOnKey(new KeyExpireDto(key.getKeyValue(), 1));
        assertEquals(result, null);
    }

    @Test
    void testSetExpireOnKeyAndThenKeyDelete() throws Exception {
        cacheRepository.putValue(key, value);
        cacheRepository.setExpireOnKey(new KeyExpireDto(key.getKeyValue(), 2));
        Thread.sleep(3000);
        assertFalse(cacheRepository.hasKey(key));
    }

    @Test
    void testSetExpireOnKeyAndThenKeyStillExists() throws Exception {
        cacheRepository.putValue(key, value);
        cacheRepository.setExpireOnKey(new KeyExpireDto(key.getKeyValue(), 4));
        Thread.sleep(3000);
        assertTrue(cacheRepository.hasKey(key));
    }
}