package ru.kh.redis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kh.redis.services.ExpireService;
import ru.kh.redis.dto.keysDto.KeyDto;
import ru.kh.redis.dto.keysDto.KeyExpireDto;

import javax.validation.Valid;

@RestController
public class ExpireController {

    private final ExpireService expireService;

    @Autowired
    public ExpireController(ExpireService expireService) {
        this.expireService = expireService;
    }

    @PostMapping("/ttl")
    public ResponseEntity<String> getTTL(@RequestBody @Valid KeyDto keyDto) {
        String result = expireService.getKeyTTL(keyDto);
        if (result == null) {
            return new ResponseEntity<>("-2", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/expire")
    public ResponseEntity<String> setExpire(@RequestBody @Valid KeyExpireDto keyExpireDto) {
        String result = expireService.expireKey(keyExpireDto);
        if (result == null) {
            return new ResponseEntity<>("0", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
