package ru.kh.redis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kh.redis.models.Key;
import ru.kh.redis.services.ExpireService;
import ru.kh.redis.dto.keysDto.KeyDto;
import ru.kh.redis.dto.keysDto.KeyExpireDto;
import ru.kh.redis.utils.ResponseErrorGenerator;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
public class ExpireController {

    private final ExpireService expireService;

    @Autowired
    public ExpireController(ExpireService expireService) {
        this.expireService = expireService;
    }

    @PostMapping("/ttl")
    public ResponseEntity<String> getTTL(@RequestBody @Valid KeyDto keyDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseErrorGenerator.generateErrorResponseByBinding(bindingResult);
        }
        Key key = Key.createKeyFromKeyDto(keyDto);
        long result = expireService.getKeyTTL(key);
        System.out.println(result);
        if (result == -1L) {
            return new ResponseEntity<>("-1", HttpStatus.OK);
        }
        if (result == -2L) {
            return new ResponseEntity<>("-2", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(result)), HttpStatus.OK);
    }

    @PostMapping("/expire")
    public ResponseEntity<String> setExpire(@RequestBody @Valid KeyExpireDto keyExpireDto,
                                            BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseErrorGenerator.generateErrorResponseByBinding(bindingResult);
        }
        int result = expireService.expireKey(keyExpireDto);
        return new ResponseEntity<>(String.valueOf(result), result == 1 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
