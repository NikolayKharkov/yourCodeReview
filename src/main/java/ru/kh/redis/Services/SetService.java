package ru.kh.redis.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kh.redis.Models.Key;
import ru.kh.redis.Models.entities.ListEntity;
import ru.kh.redis.Models.entities.SetEntity;
import ru.kh.redis.Models.entities.StoredEntity;
import ru.kh.redis.Repositories.CacheRepository;
import ru.kh.redis.dto.setsDto.SetHgetDto;
import ru.kh.redis.dto.setsDto.SetHsetDto;
import ru.kh.redis.utils.Consts;

@Service
public class SetService {

    private final CacheRepository cacheRepository;

    @Autowired
    public SetService(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    public String setEntryValuesFields(SetHsetDto setHsetDto) {
        Key key = new Key(setHsetDto.getKey());
        if (isKeyExist(key)) {
            StoredEntity value = cacheRepository.getValue(key);
            if (!isValueSetEntity(value)) {
                return Consts.ERROR_MESSAGE_WRONG_TYPE;
            }
            return String.valueOf(((SetEntity) value).putsKeysFields(setHsetDto.getFieldsValuesEntry().get(0)));
        }
        SetEntity setEntity = new SetEntity(setHsetDto.getFieldsValuesEntry().get(0));
        cacheRepository.putValue(key, setEntity);
        return String.valueOf(setEntity.getSize());
    }

    public String getFieldByKeySet(SetHgetDto setHgetDto) {
        Key key = new Key(setHgetDto.getKey());
        if (isKeyExist(key)) {
            StoredEntity value = cacheRepository.getValue(key);
            if (!isValueSetEntity(value)) {
                return Consts.ERROR_MESSAGE_WRONG_TYPE;
            }
            return String.valueOf(((SetEntity) value).getFieldByKeySet(setHgetDto.getKeySet()));
        }
        return Consts.ERROR_MESSAGE_KEY_NOT_FOUND;
    }

    private boolean isValueSetEntity(StoredEntity value) {
        return value instanceof SetEntity;
    }

    private boolean isKeyExist(Key key) {
        return cacheRepository.hasKey(key);
    }

    private String validateValueByKey(Key key) {
        if (!isKeyExist(key)) {
            return "(nil)";
        }
        if (!isValueSetEntity(cacheRepository.getValue(key))) {
            return Consts.ERROR_MESSAGE_WRONG_TYPE;
        }
        return "OK";
    }
}
