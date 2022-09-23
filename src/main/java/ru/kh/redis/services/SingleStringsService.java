package ru.kh.redis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kh.redis.models.Key;
import ru.kh.redis.models.entities.StoredEntity;
import ru.kh.redis.models.entities.StringEntity;
import ru.kh.redis.repositories.CacheRepository;
import ru.kh.redis.utils.Consts;

@Service
public class SingleStringsService {

    private final CacheRepository cacheRepository;

    @Autowired
    public SingleStringsService(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    public String getStringValueByKey(Key key) {
        if (!isKeyExist(key)) {
            return Consts.ERROR_MESSAGE_KEY_NOT_FOUND;
        }
        StoredEntity value = cacheRepository.getValue(key);
        if (!isValueStringEntity(value)) {
            return Consts.ERROR_MESSAGE_WRONG_TYPE;
        }
        return value.getValue().toString();
    }

    public String setValue(Key key, StringEntity value) {
        if (isKeyExist(key)) {
            if (isValueStringEntity(cacheRepository.getValue(key))) {
                cacheRepository.putValue(key, value);
                return "OK";
            }
            return Consts.ERROR_MESSAGE_WRONG_TYPE;
        }
        cacheRepository.putValue(key, value);
        return "OK";
    }

    private boolean isValueStringEntity(StoredEntity value) {
        return value instanceof StringEntity;
    }

    private boolean isKeyExist(Key key) {
        return cacheRepository.hasKey(key);
    }
}
