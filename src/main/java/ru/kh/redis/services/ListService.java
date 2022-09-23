package ru.kh.redis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kh.redis.models.Key;
import ru.kh.redis.models.entities.ListEntity;
import ru.kh.redis.models.entities.StoredEntity;
import ru.kh.redis.repositories.CacheRepository;
import ru.kh.redis.dto.listsDto.ListEntityDto;
import ru.kh.redis.dto.listsDto.ListLGetDto;
import ru.kh.redis.dto.listsDto.ListLindexDto;
import ru.kh.redis.dto.listsDto.ListLsetDto;
import ru.kh.redis.utils.Consts;

import java.util.List;

@Service
public class ListService {

    private final CacheRepository cacheRepository;

    @Autowired
    public ListService(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    public String pushValuesOrCreate(ListEntityDto listEntityDto) {
        Key key = new Key(listEntityDto.getKey());
        if (isKeyExist(key)) {
            StoredEntity value = cacheRepository.getValue(key);
            if (!isValueListEntity(value)) {
                return Consts.ERROR_MESSAGE_WRONG_TYPE;
            }
            return String.valueOf(((ListEntity) value).lpush(listEntityDto.getValues()));
        }
        ListEntity listEntity = new ListEntity(listEntityDto.getValues());
        cacheRepository.putValue(key, listEntity);
        return String.valueOf(listEntity.getSize());
    }

    public String getListSize(Key key) {
        String result = validateValueByKey(key);
        if (result.equals("OK")) {
            result = String.valueOf(((ListEntity) cacheRepository.getValue(key)).getSize());
        }
        return result;
    }

    public String replaceValueByIndex(ListLsetDto listLsetDto) {
        Key key = new Key(listLsetDto.getKey());
        String result = validateValueByKey(key);
        if (result.equals("OK")) {
            result = String.valueOf(((ListEntity) cacheRepository.getValue(key))
                    .lset(listLsetDto.getIndex(), listLsetDto.getValue()));
        }
        return result;
    }

    public String getElementFromListByKey(ListLindexDto listLindexDto) {
        Key key = new Key(listLindexDto.getKey());
        String result = validateValueByKey(key);
        if (result.equals("OK")) {
            try {
                ListEntity listEntity = (ListEntity) cacheRepository.getValue(key);
                result = listEntity.lindex(listLindexDto.getIndex());
            } catch (IndexOutOfBoundsException exception) {
                result = Consts.ERROR_MESSAGE_INDEX_BOUND_OF_ARRAY;
            } catch (NullPointerException exception) {
                result = Consts.ERROR_MESSAGE_KEY_NOT_FOUND;
            }
        }
        return result;
    }

    public String getElementsByStartAndFinishIndices(ListLGetDto listLGetDto) {
        Key key = new Key(listLGetDto.getKey());
        String result = validateValueByKey(key);
        if (result.equals("OK")) {
            try {
                ListEntity listEntity = (ListEntity) cacheRepository.getValue(key);
                List<String> elements = listEntity.lget(listLGetDto.getStartIndex(), listLGetDto.getFinishIndex());
                StringBuilder outPut = new StringBuilder();
                for (int i = 0; i < elements.size(); i++) {
                    outPut
                            .append(i + 1).append(") ")
                            .append(elements.get(i))
                            .append("\n");
                }
                result = outPut.toString();
            } catch (IndexOutOfBoundsException exception) {
                result = Consts.ERROR_MESSAGE_INDEX_BOUND_OF_ARRAY;
            } catch (NullPointerException exception) {
                result = Consts.ERROR_MESSAGE_KEY_NOT_FOUND;
            }
        }
        return result;
    }

    private boolean isValueListEntity(StoredEntity value) {
        return value instanceof ListEntity;
    }

    private boolean isKeyExist(Key key) {
        return cacheRepository.hasKey(key);
    }

    private String validateValueByKey(Key key) {
        if (!isKeyExist(key)) {
            return "(nil)";
        }
        if (!isValueListEntity(cacheRepository.getValue(key))) {
            return Consts.ERROR_MESSAGE_WRONG_TYPE;
        }
        return "OK";
    }
}
