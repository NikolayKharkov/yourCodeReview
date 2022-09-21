package ru.kh.redis.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class KeyExpireDto {

    @NotNull(message = "Key can't be null")
    @NotEmpty(message = "Key can't be empty")
    private String key;
    @NotNull(message = "Time not specified")
    @Min(value = 0, message = "Time must be greater than zero")
    private int seconds;

    public KeyExpireDto() {
    }

    public KeyExpireDto(String key, int seconds) {
        this.key = key;
        this.seconds = seconds;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
