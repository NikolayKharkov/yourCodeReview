package ru.kh.redis.Models.entities;

import java.util.ArrayList;
import java.util.List;

public class ListEntity implements StoredEntity<List<String>> {

    private List<String> value = new ArrayList<>();

    public ListEntity() {
    }

    public ListEntity(List<String> value) {
        this.value = value;
    }

    @Override
    public List<String> getValue() {
        return value;
    }

    @Override
    public void setValue(List<String> value) {
        this.value = value;
    }

    public int getSize() {
        return value.size();
    }

    public int lpush(List<String> elements) {
        value.addAll(0, elements);
        return value.size();
    }

    public int lset(int index, String element) {
        if (index >= value.size() || index < 0) {
            return -1;
        }
        value.add(index, element);
        return value.size();
    }

    public String lindex(int index) {
        if (index < 0) {
            index = (getSize() - Math.abs(index));
        }
        if (index >= getSize() || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return String.valueOf(value.get(index));

    }

    public List<String> lget(int startIndex, int finishIndex) {
        if (startIndex < 0) {
            startIndex = (getSize() - Math.abs(startIndex));
        }
        if (finishIndex < 0) {
            finishIndex = (getSize() - Math.abs(finishIndex));
        }
        if (startIndex >= getSize() || finishIndex >= getSize() || startIndex > finishIndex ||
                startIndex < 0 || finishIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        List<String> result = new ArrayList<>();
        for (int i = startIndex; i <= finishIndex; i++) {
            result.add(value.get(i));
        }
        return result;
    }
}
