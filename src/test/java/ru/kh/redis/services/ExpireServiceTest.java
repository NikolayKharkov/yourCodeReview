package ru.kh.redis.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kh.redis.dto.keysDto.KeyDto;
import ru.kh.redis.models.Key;
import ru.kh.redis.models.entities.StringEntity;
import ru.kh.redis.dto.keysDto.KeyExpireDto;
import ru.kh.redis.utils.Consts;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExpireServiceTest {

    @Autowired
    SingleStringsService stringsService;

    @Autowired
    ExpireService expireService;

    final String RESULT_WHEN_KEY_FOUND = "1";

    final String RESULT_WHEN_KEY_NOT_EXPIRE = "-1";

    Key defaultKey = new Key("key");

    KeyDto defaultKeyDto = new KeyDto("key");

    StringEntity defaultStringEntity = new StringEntity("value");

    String defaultKeyValue = "key";

    String defaultValue = "value";

    @Test
    void testTtlWhenKeyIsExistButNotExpired() {
        stringsService.setValue(defaultKey, defaultStringEntity);
        String result = expireService.getKeyTTL(defaultKeyDto);
        assertEquals(result, RESULT_WHEN_KEY_NOT_EXPIRE);
    }

    @Test
    void testTtlWhenKeyIsNotExist() {
        String result = expireService.getKeyTTL(defaultKeyDto);
        assertEquals(result, null);
    }

    @Test
    void testExpireWhenKeyIsNotExist() {
        KeyExpireDto keyExpireDto = new KeyExpireDto(defaultKeyValue, 6);
        String result = expireService.expireKey(keyExpireDto);
        assertEquals(result, null);
    }

    @Test
    void testExpireWhenKeyIsExist() {
        stringsService.setValue(defaultKey, defaultStringEntity);
        KeyExpireDto keyExpireDto = new KeyExpireDto(defaultKeyValue, 6);
        String result = expireService.expireKey(keyExpireDto);
        assertEquals(result, RESULT_WHEN_KEY_FOUND);
    }

    @Test
    void testExpireWhenKeySetExpireButStillExist() throws InterruptedException {
        stringsService.setValue(defaultKey, defaultStringEntity);
        KeyExpireDto keyExpireDto = new KeyExpireDto(defaultKeyValue, 10);
        expireService.expireKey(keyExpireDto);
        Thread.sleep(5000);
        String result = stringsService.getStringValueByKey(defaultKeyDto);
        assertEquals(result, defaultValue);
    }

    @Test
    void testExpireWhenKeySetExpireThenWasRemoved() throws InterruptedException {
        stringsService.setValue(defaultKey, defaultStringEntity);
        KeyExpireDto keyExpireDto = new KeyExpireDto(defaultKeyValue, 1);
        expireService.expireKey(keyExpireDto);
        Thread.sleep(2000);
        String result = stringsService.getStringValueByKey(defaultKeyDto);
        assertEquals(result, Consts.ERROR_MESSAGE_KEY_NOT_FOUND);
    }

    @Test
    void testTtlTime() throws InterruptedException {
        stringsService.setValue(defaultKey, defaultStringEntity);
        KeyExpireDto keyExpireDto = new KeyExpireDto(defaultKeyValue, 10);
        expireService.expireKey(keyExpireDto);
        Thread.sleep(2000);
        long result = TimeUnit.MILLISECONDS.toSeconds(Long.valueOf(expireService.getKeyTTL(defaultKeyDto)));
        assertTrue(result <= 8L);
    }
}