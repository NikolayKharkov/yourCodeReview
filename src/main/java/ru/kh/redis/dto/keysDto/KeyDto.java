package ru.kh.redis.dto.keysDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class KeyDto {

    @NotNull(message = "Key can't be null")
    @NotEmpty(message = "Key can't be empty")
    private String key;

    public KeyDto() {
    }

    public KeyDto(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
