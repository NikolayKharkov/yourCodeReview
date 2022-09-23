package ru.kh.redis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kh.redis.models.Key;
import ru.kh.redis.repositories.CacheRepository;
import ru.kh.redis.dto.keysDto.KeyExpireDto;

@Service
public class ExpireService {

    private final CacheRepository cacheRepository;

    @Autowired
    public ExpireService(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
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
}
