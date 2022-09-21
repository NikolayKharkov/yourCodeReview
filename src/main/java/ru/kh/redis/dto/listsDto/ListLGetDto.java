package ru.kh.redis.dto.listsDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ListLGetDto {

    @NotNull(message = "Key can't be null")
    @NotEmpty(message = "Key can't be empty")
    private String key;
    @NotNull(message = "Start index can't be null")
    private int startIndex;
    @NotNull(message = "Finish index can't be null")
    private int finishIndex;

    public ListLGetDto() {

    }

    public ListLGetDto(String key, int startIndex, int finishIndex) {
        this.key = key;
        this.startIndex = startIndex;
        this.finishIndex = finishIndex;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getFinishIndex() {
        return finishIndex;
    }

    public void setFinishIndex(int finishIndex) {
        this.finishIndex = finishIndex;
    }
}
