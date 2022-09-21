package ru.kh.redis.Models.entities;

public interface StoredEntity <V> {
    V getValue();
    void setValue(V value);
}
