package ru.kh.redis.dto.listsDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ListLsetDto {

    @NotNull(message = "Key can't be null")
    @NotEmpty(message = "Key can't be empty")
    private String key;
    @NotNull(message = "Value can't be null")
    @NotEmpty(message = "Value can't be empty")
    private String value;
    @NotNull(message = "Index can't be null")
    @Min(value = 0, message = "Index can't be negative")
    private int index;

    public ListLsetDto() {

    }

    public ListLsetDto(String key, String value, int index) {
        this.key = key;
        this.value = value;
        this.index = index;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
