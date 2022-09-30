package ru.kh.redis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kh.redis.models.Key;
import ru.kh.redis.models.entities.StringEntity;
import ru.kh.redis.services.SingleStringsService;
import ru.kh.redis.dto.keysDto.KeyDto;
import ru.kh.redis.dto.stringsDto.StringStoredEntityDto;
import ru.kh.redis.utils.ResponseErrorGenerator;

import javax.validation.Valid;

@RestController
public class StringValuesController {

    private final SingleStringsService singleStringsService;

    @Autowired
    public StringValuesController(SingleStringsService singleStringsService) {
        this.singleStringsService = singleStringsService;
    }

    @PostMapping("/get")
    public ResponseEntity<String> get(@RequestBody @Valid KeyDto keyDto) {
        String result = singleStringsService.getStringValueByKey(keyDto);
        ResponseEntity<String> responseError = ResponseErrorGenerator
                .validateResultForKeyNotFoundOrWrongTypeErrors(result);
        if (responseError != null) {
            return responseError;
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/set")
    public ResponseEntity<String> set(@RequestBody @Valid StringStoredEntityDto storedEntityDto) {
        String result = singleStringsService.setValue(new Key(storedEntityDto.getKey()),
                new StringEntity(storedEntityDto.getValue()));
        ResponseEntity<String> responseError = ResponseErrorGenerator
                .validateResultForKeyNotFoundOrWrongTypeErrors(result);
        if (responseError != null) {
            return responseError;
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
