package ru.kh.redis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kh.redis.services.KeysService;
import ru.kh.redis.dto.keysDto.KeysDto;
import ru.kh.redis.dto.pattrensDto.PatternDto;
import ru.kh.redis.utils.ResponseErrorGenerator;

import javax.validation.Valid;


@RestController
public class KeysController {

    private final KeysService keysService;

    @Autowired
    public KeysController(KeysService keysService) {
        this.keysService = keysService;
    }

    @PostMapping("/del")
    public ResponseEntity<String> del(@RequestBody @Valid KeysDto keys, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseErrorGenerator.generateErrorResponseByBinding(bindingResult);
        }
        int countDeleted = keysService.delKeys(keys.getKeys());
        String result = new StringBuilder()
                .append("(integer) ")
                .append(countDeleted)
                .toString();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/keys")
    public ResponseEntity<String> keys(@RequestBody @Valid PatternDto patternDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseErrorGenerator.generateErrorResponseByBinding(bindingResult);
        }
        String keys = keysService.getKeys(patternDto.getPattern());
        return new ResponseEntity<>(keys, HttpStatus.OK);
    }
}
