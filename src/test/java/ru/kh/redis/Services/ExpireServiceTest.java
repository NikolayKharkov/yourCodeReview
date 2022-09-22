package ru.kh.redis.Services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.kh.redis.Models.Key;
import ru.kh.redis.Models.entities.StringEntity;
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

    final long RESULT_WHEN_KEY_NOT_FOUND = 0L;

    final long RESULT_WHEN_KEY_FOUND = 1L;

    final long RESULT_WHEN_KEY_NOT_EXPIRE = -1L;

    final long RESULT_WHEN_KEY_EXPIRE_NOT_EXIST = -2L;

    Key defaultKey = new Key("key");

    StringEntity defaultStringEntity = new StringEntity("value");

    String defaultKeyValue = "key";

    String defaultValue = "value";

    @Test
    public void testTtlWhenKeyIsExistButNotExpired() {
        stringsService.setValue(defaultKey, defaultStringEntity);
        long result = expireService.getKeyTTL(defaultKey);
        assertEquals(result, RESULT_WHEN_KEY_NOT_EXPIRE);
    }

    @Test
    public void testTtlWhenKeyIsNotExist() {
        long result = expireService.getKeyTTL(defaultKey);
        assertEquals(result, RESULT_WHEN_KEY_EXPIRE_NOT_EXIST);
    }

    @Test
    public void testExpireWhenKeyIsNotExist() {
        KeyExpireDto keyExpireDto = new KeyExpireDto(defaultKeyValue, 6);
        long result = expireService.expireKey(keyExpireDto);
        assertEquals(result, RESULT_WHEN_KEY_NOT_FOUND);
    }

    @Test
    public void testExpireWhenKeyIsExist() {
        stringsService.setValue(defaultKey, defaultStringEntity);
        KeyExpireDto keyExpireDto = new KeyExpireDto(defaultKeyValue, 6);
        long result = expireService.expireKey(keyExpireDto);
        assertEquals(result, RESULT_WHEN_KEY_FOUND);
    }

    @Test
    public void testExpireWhenKeySetExpireButStillExist() throws InterruptedException {
        stringsService.setValue(defaultKey, defaultStringEntity);
        KeyExpireDto keyExpireDto = new KeyExpireDto(defaultKeyValue, 10);
        expireService.expireKey(keyExpireDto);
        Thread.sleep(5000);
        String result = stringsService.getStringValueByKey(defaultKey);
        assertEquals(result, defaultValue);
    }

    @Test
    public void testExpireWhenKeySetExpireThenWasRemoved() throws InterruptedException {
        stringsService.setValue(defaultKey, defaultStringEntity);
        KeyExpireDto keyExpireDto = new KeyExpireDto(defaultKeyValue, 1);
        expireService.expireKey(keyExpireDto);
        Thread.sleep(2000);
        String result = stringsService.getStringValueByKey(defaultKey);
        assertEquals(result, Consts.ERROR_MESSAGE_KEY_NOT_FOUND);
    }

    @Test
    public void testTtlTime() throws InterruptedException {
        stringsService.setValue(defaultKey, defaultStringEntity);
        KeyExpireDto keyExpireDto = new KeyExpireDto(defaultKeyValue, 10);
        expireService.expireKey(keyExpireDto);
        Thread.sleep(2000);
        long result = TimeUnit.MILLISECONDS.toSeconds(expireService.getKeyTTL(defaultKey));
        assertTrue(result <= 8L);
    }
}