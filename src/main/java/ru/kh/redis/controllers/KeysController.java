package ru.kh.redis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kh.redis.services.KeysService;
import ru.kh.redis.dto.keysDto.KeysDto;
import ru.kh.redis.dto.pattrensDto.PatternDto;

import javax.validation.Valid;


@RestController
public class KeysController {

    private final KeysService keysService;

    @Autowired
    public KeysController(KeysService keysService) {
        this.keysService = keysService;
    }

    @PostMapping("/del")
    public ResponseEntity<String> del(@RequestBody @Valid KeysDto keys) {
        int countDeleted = keysService.delKeys(keys.getKeys());
        String result = "(integer) " + countDeleted;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/keys")
    public ResponseEntity<String> keys(@RequestBody @Valid PatternDto patternDto) {
        String keys = keysService.getKeys(patternDto.getPattern());
        return new ResponseEntity<>(keys, HttpStatus.OK);
    }
}
