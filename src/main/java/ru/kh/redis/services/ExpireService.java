package ru.kh.redis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kh.redis.dto.keysDto.KeyDto;
import ru.kh.redis.models.Key;
import ru.kh.redis.repositories.CacheRepository;
import ru.kh.redis.dto.keysDto.KeyExpireDto;

import java.util.concurrent.TimeUnit;

@Service
public class ExpireService {

    private final CacheRepository cacheRepository;

    @Autowired
    public ExpireService(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    public String expireKey(KeyExpireDto keyExpireDto) {
        return cacheRepository.setExpireOnKey(keyExpireDto);
    }

    public String getKeyTTL(KeyDto keyDto) {
        Key key = cacheRepository.getKey(Key.createKeyFromKeyDto(keyDto));
        if (key == null) {
            return null;
        }
        if (key.isHasExpirationTime()) {
            return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(key.getTTL()));
        }
        return "-1";
    }
}
