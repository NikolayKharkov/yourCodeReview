package ru.kh.redis.models.entities;

public interface StoredEntity <V> {
    V getValue();
    void setValue(V value);
}
