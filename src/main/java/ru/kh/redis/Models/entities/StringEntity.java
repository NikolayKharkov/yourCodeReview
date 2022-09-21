package ru.kh.redis.Models.entities;

public class StringEntity implements StoredEntity<String> {

    private String value;

    public StringEntity() {

    }

    public StringEntity(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
