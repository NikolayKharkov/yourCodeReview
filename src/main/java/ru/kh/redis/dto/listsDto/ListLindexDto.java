package ru.kh.redis.dto.listsDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ListLindexDto {

    @NotNull(message = "Key can't be null")
    @NotEmpty(message = "Key can't be empty")
    private String key;
    @NotNull(message = "Index can't be null")
    private int index;

    public ListLindexDto() {

    }

    public ListLindexDto(String key, int index) {
        this.key = key;
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
