package ru.kh.redis.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kh.redis.Models.Key;
import ru.kh.redis.Repositories.CacheRepository;
import ru.kh.redis.dto.KeyExpireDto;

import java.util.List;

@Service
public class CacheService {

    private final CacheRepository cacheRepository;
    private final String ERROR_MESSAGE_WRONG_TYPE = "WRONGTYPE Operation against a key holding a wrong type";

    @Autowired
    public CacheService(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    public String getStringValueByKey(Key key) {
        if (!cacheRepository.hasKey(key)) {
            return "(nil)";
        }
        Object value = cacheRepository.getValue(key);
        if (!(value instanceof String)) {
            return ERROR_MESSAGE_WRONG_TYPE;
        }
        return value.toString();
    }

    public void setValue(Key key, Object value) {
        cacheRepository.putValue(key, value);
    }

    public Boolean isKeyExist(Key key) {
        return cacheRepository.hasKey(key);
    }

    public int delKeys(String[] keys) {
        return cacheRepository.removesKeys(keys);
    }

    public String getKeys(String pattern) {
        StringBuilder result = new StringBuilder();
        List<String> keys = cacheRepository.getKeysByPattern(pattern);
        if (keys == null || keys.size() == 0) {
            result.append("No matchers");
        }
        for (int i = 0; i < keys.size(); i++) {
            result
                    .append((i + 1))
                    .append(" ) ")
                    .append(keys.get(i))
                    .append("\n");
        }
        return result.toString();
    }

    public Key getKey(Key key) {
        return cacheRepository.getKey(key);
    }

    public int expireKey(KeyExpireDto keyExpireDto) {
        return cacheRepository.setExpireOnKey(keyExpireDto);
    }

    public long getKeyTTL(Key key) {
        key = cacheRepository.getKey(key);
        if (key == null) {
            return -2L;
        }
        if (!key.isHasExpirationTime()) {
            return -1L;
        }
        return key.getTTL();
    }

    public String getERROR_MESSAGE_WRONG_TYPE() {
        return ERROR_MESSAGE_WRONG_TYPE;
    }
}
