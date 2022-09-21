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
import ru.kh.redis.dto.StringStoredEntityDto;
import ru.kh.redis.utils.ResponseErrorGenerator;

import javax.validation.Valid;

@RestController
public class StringValuesController {

    private final CacheService cacheService;

    @Autowired
    public StringValuesController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/get")
    public ResponseEntity<String> get(@RequestBody @Valid KeyDto keyDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
           return ResponseErrorGenerator.generateErrorResponse(bindingResult);
        }
        Key key = Key.createKeyFromKeyDto(keyDto);
        String value = cacheService.getStringValueByKey(key);
        return new ResponseEntity<>(value,
                value.equals(cacheService.getERROR_MESSAGE_WRONG_TYPE()) ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @PostMapping("/set")
    public ResponseEntity<String> set(@RequestBody @Valid StringStoredEntityDto storedEntityDto,
                                      BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseErrorGenerator.generateErrorResponse(bindingResult);
        }
        cacheService.setValue(new Key(storedEntityDto.getKey()), storedEntityDto.getValue());
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
