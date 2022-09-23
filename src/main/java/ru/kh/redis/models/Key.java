package ru.kh.redis.models;

import ru.kh.redis.dto.keysDto.KeyDto;

import java.util.Objects;

public class Key {

    private final String keyValue;
    private boolean hasExpirationTime;
    private long delayTime;

    public Key(String keyValue) {
        this.keyValue = keyValue;
    }

    public static Key createKeyFromKeyDto(KeyDto keyDto) {
        return new Key(keyDto.getKey());
    }

    public String getKeyValue() {
        return keyValue;
    }

    public boolean isHasExpirationTime() {
        return hasExpirationTime;
    }

    public void setHasExpirationTime(boolean hasExpirationTime) {
        this.hasExpirationTime = hasExpirationTime;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public long getTTL() {
        return hasExpirationTime ? delayTime - System.currentTimeMillis() : -1L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return Objects.equals(keyValue, key.keyValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyValue);
    }
}
