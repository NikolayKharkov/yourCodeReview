package ru.kh.redis.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class StringStoredEntityDto {

    @NotNull(message = "Key can't be null")
    @NotEmpty(message = "Key can't be empty")
    private String key;

    @NotNull(message = "Value can't be null")
    @NotEmpty(message = "Value can't be empty")
    private String value;

    public StringStoredEntityDto() {

    }

    public StringStoredEntityDto(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
