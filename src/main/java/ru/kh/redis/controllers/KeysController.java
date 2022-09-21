package ru.kh.redis.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kh.redis.Services.CacheService;
import ru.kh.redis.dto.KeysDto;
import ru.kh.redis.dto.PatternDto;
import ru.kh.redis.dto.StringStoredEntityDto;
import ru.kh.redis.utils.ResponseErrorGenerator;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class KeysController {

    private final CacheService cacheService;

    @Autowired
    public KeysController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/del")
    public ResponseEntity<String> del(@RequestBody @Valid KeysDto keys, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseErrorGenerator.generateErrorResponse(bindingResult);
        }
        int countDeleted = cacheService.delKeys(keys.getKeys());
        String result = new StringBuilder()
                .append("(integer) ")
                .append(countDeleted)
                .toString();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/keys")
    public ResponseEntity<String> keys(@RequestBody @Valid PatternDto patternDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseErrorGenerator.generateErrorResponse(bindingResult);
        }
        String keys = cacheService.getKeys(patternDto.getPattern());
        return new ResponseEntity<>(keys, HttpStatus.OK);
    }
}
