package ru.kh.redis.Models.entities;

import ru.kh.redis.utils.Consts;

import java.util.HashMap;

public class SetEntity implements StoredEntity<HashMap<String, String>> {

    private HashMap<String, String> value = new HashMap<>();

    public SetEntity() {
    }

    public SetEntity(HashMap<String, String> value) {
        this.value = value;
    }

    @Override
    public HashMap<String, String> getValue() {
        return value;
    }

    @Override
    public void setValue(HashMap<String, String> value) {
        this.value = value;
    }

    public int putsKeysFields(HashMap<String, String> keysFields) {
        value.putAll(keysFields);
        return keysFields.size();
    }

    public String getFieldByKeySet(String keySet) {
        String result = value.get(keySet);
        return result == null ? Consts.ERROR_MESSAGE_KEY_NOT_FOUND : result;
    }

    public int getSize() {
        return value.size();
    }
}
