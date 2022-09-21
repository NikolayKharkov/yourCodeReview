package ru.kh.redis.dto.setsDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SetHsetDto {

    @NotNull(message = "Key can't be null")
    @NotEmpty(message = "Key can't be empty")
    private String key;

    @NotNull(message = "Fields values entry can't be null")
    @NotEmpty(message = "Fields values entry can't be empty")
    private List<HashMap<String, String>> fieldsValuesEntry = new ArrayList<>();

    public SetHsetDto() {
    }

    public SetHsetDto(String key, List<HashMap<String, String>> fieldsValuesEntry) {
        this.key = key;
        this.fieldsValuesEntry = fieldsValuesEntry;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<HashMap<String, String>> getFieldsValuesEntry() {
        return fieldsValuesEntry;
    }

    public void setFieldsValuesEntry(List<HashMap<String, String>> fieldsValuesEntry) {
        this.fieldsValuesEntry = fieldsValuesEntry;
    }
}
