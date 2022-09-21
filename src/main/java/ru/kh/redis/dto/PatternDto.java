package ru.kh.redis.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PatternDto {

    @NotNull(message = "Pattern not specified")
    @NotEmpty(message = "Pattern not specified")
    private String pattern;

    public PatternDto() {
    }

    public PatternDto(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String generateRegex() {
        return  pattern
                .replace("*", "(\\w*)")
                .replace("?", "(\\w)");
    }
}
