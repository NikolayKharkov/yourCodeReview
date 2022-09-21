package ru.kh.redis.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class KeysDto {

    @NotNull(message = "Keys not specified")
    @NotEmpty(message = "Keys not specified")
    private String[] keys;

    public KeysDto() {
    }

    public KeysDto(String[] keys) {
        this.keys = keys;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }
}
