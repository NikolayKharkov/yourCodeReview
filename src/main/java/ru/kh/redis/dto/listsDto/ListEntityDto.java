package ru.kh.redis.dto.listsDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ListEntityDto {

    @NotNull(message = "Key can't be null")
    @NotEmpty(message = "Key can't be empty")
    private String key;

    @NotNull(message = "Values not specified")
    @NotEmpty(message = "Values not specified")
    private List<String> values;

    public ListEntityDto() {

    }

    public ListEntityDto(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
