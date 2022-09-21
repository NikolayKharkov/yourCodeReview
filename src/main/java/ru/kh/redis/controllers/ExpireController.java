package ru.kh.redis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kh.redis.Models.Key;
import ru.kh.redis.Services.CacheService;
import ru.kh.redis.dto.KeyDto;
import ru.kh.redis.dto.KeyExpireDto;
import ru.kh.redis.utils.ResponseErrorGenerator;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
public class ExpireController {

    private final CacheService cacheService;

    @Autowired
    public ExpireController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/ttl")
    public ResponseEntity<String> getTTL(@RequestBody @Valid KeyDto keyDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseErrorGenerator.generateErrorResponse(bindingResult);
        }
        Key key = Key.createKeyFromKeyDto(keyDto);
        long result = cacheService.getKeyTTL(key);
        return new ResponseEntity<>(String.valueOf(result >= 0 ? TimeUnit.MILLISECONDS.toSeconds(result) : result),
                result >= -2L ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/expire")
    public ResponseEntity<String> setExpire(@RequestBody KeyExpireDto keyExpireDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseErrorGenerator.generateErrorResponse(bindingResult);
        }
        int result = cacheService.expireKey(keyExpireDto);
        return new ResponseEntity<>(String.valueOf(result), result == 1 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
