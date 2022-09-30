package ru.kh.redis.repositories;

import org.springframework.stereotype.Repository;
import ru.kh.redis.models.Key;
import ru.kh.redis.models.entities.StoredEntity;
import ru.kh.redis.dto.keysDto.KeyExpireDto;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Repository
public class CacheRepository {

    private SoftReference<ConcurrentHashMap<Key, StoredEntity>> cacheMapRef =
            new SoftReference<>(new ConcurrentHashMap<>());

    public StoredEntity getValue(Key key) {
        return (StoredEntity) function(inputKey -> cacheMapRef.get().get(inputKey), key);
    }

    public void putValue(Key key, StoredEntity value) {
        consumer((inputKey, inputValue) -> cacheMapRef.get().put(inputKey, inputValue), key, value);
    }

    public boolean hasKey(Key key) {
        return predicate(keyValue -> cacheMapRef.get().containsKey(keyValue), key);
    }

    public String setExpireOnKey(KeyExpireDto keyExpireDto) {
        Key key = getKey(new Key(keyExpireDto.getKey()));
        if (key == null) {
            return null;
        }
        key.setHasExpirationTime(true);
        long expireSeconds = TimeUnit.SECONDS.toMillis(keyExpireDto.getSeconds());
        key.setDelayTime(System.currentTimeMillis() + expireSeconds);
        consumer((expiredKey, expiredTime) -> {
            TimerTask task = new TimerTask() {
                public void run() {
                    cacheMapRef.get().remove(expiredKey);
                }
            };
            Timer timer = new Timer(expiredKey.getKeyValue());
            timer.schedule(task, expiredTime);
        }, key, expireSeconds);
        return "1";
    }

    private boolean removeKey(Key key) {
        return predicate(keyValue -> {
            if (hasKey(keyValue)) {
                cacheMapRef.get().remove(keyValue);
                return true;
            }
            return false;
        }, key);
    }

    public int removesKeys(String[] keys) {
        int result = 0;
        for (String key : keys) {
            result += removeKey(new Key(key)) ? 1 : 0;
        }
        return result;
    }

    public List<String> getKeysByPattern(String pattern) {
        String regex = pattern
                .replace("*", "(\\w*)")
                .replace("?", "(\\w)");
        return (List<String>) function(searchPattern -> {
            List<String> keys = new ArrayList<>();
            for (Key key : cacheMapRef.get().keySet()) {
                String keyValue = key.getKeyValue();
                if (keyValue.matches(searchPattern)) {
                    keys.add(keyValue);
                }
            }
            return keys;
        }, regex);
    }

    public Key getKey(Key searchingKey) {
        return (Key) function(k -> {
            for (Key storedKey : cacheMapRef.get().keySet()) {
                if (storedKey.equals(k)) {
                    return storedKey;
                }
            }
            return null;
        }, searchingKey);
    }

    private void rebuildCacheMap() {
        cacheMapRef = new SoftReference<>(new ConcurrentHashMap<>());
    }

    private <T> Object function(Function<T, Object> function, T tParam) {
        try {
            return function.apply(tParam);
        } catch (NullPointerException nullException) {
            rebuildCacheMap();
            return function(function, tParam);
        }
    }

    private <T> boolean predicate(Predicate<T> predicate, T tParam) {
        try {
            return predicate.test(tParam);
        } catch (NullPointerException nullException) {
            rebuildCacheMap();
            return false;
        }
    }

    private <T, V> void consumer(BiConsumer<T, V> biConsumer, T tParam, V vParam) {
        try {
             biConsumer.accept(tParam, vParam);
        } catch (NullPointerException nullException) {
            rebuildCacheMap();
            consumer(biConsumer, tParam, vParam);
        }
    }
}
