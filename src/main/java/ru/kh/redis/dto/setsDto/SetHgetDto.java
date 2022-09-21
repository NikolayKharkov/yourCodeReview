package ru.kh.redis.dto.setsDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SetHgetDto {

    @NotNull(message = "Key can't be null")
    @NotEmpty(message = "Key can't be empty")
    private String key;

    @NotNull(message = "Key set can't be null")
    @NotEmpty(message = "Key set can't be empty")
    private String keySet;

    public SetHgetDto() {
    }

    public SetHgetDto(String key, String keySet) {
        this.key = key;
        this.keySet = keySet;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeySet() {
        return keySet;
    }

    public void setKeySet(String keySet) {
        this.keySet = keySet;
    }
}
