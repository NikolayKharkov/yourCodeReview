package ru.kh.redis.dto.setsDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

public class SetHsetDto {

    @NotNull(message = "Key can't be null")
    @NotEmpty(message = "Key can't be empty")
    private String key;

    @NotNull(message = "Fields values entry can't be null")
    @NotEmpty(message = "Fields values entry can't be empty")
    private HashMap<String, String> fieldsValuesEntry = new HashMap<>();

    public SetHsetDto() {
    }

    public SetHsetDto(String key, HashMap<String, String> fieldsValuesEntry) {
        this.key = key;
        this.fieldsValuesEntry = fieldsValuesEntry;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HashMap<String, String> getFieldsValuesEntry() {
        return fieldsValuesEntry;
    }

    public void setFieldsValuesEntry(HashMap<String, String> fieldsValuesEntry) {
        this.fieldsValuesEntry = fieldsValuesEntry;
    }
}
