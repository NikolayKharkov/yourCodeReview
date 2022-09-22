package ru.kh.redis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kh.redis.Services.SetService;
import ru.kh.redis.dto.setsDto.SetHgetDto;
import ru.kh.redis.dto.setsDto.SetHsetDto;
import ru.kh.redis.utils.ResponseErrorGenerator;

import javax.validation.Valid;

@RestController
public class SetValuesController {

    private final SetService setService;

    @Autowired
    public SetValuesController(SetService setService) {
        this.setService = setService;
    }

    @PostMapping("/hset")
    public ResponseEntity<String> hset(@RequestBody @Valid SetHsetDto setHsetDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseErrorGenerator.generateErrorResponseByBinding(bindingResult);
        }
        String result = setService.setEntryValuesFields(setHsetDto);
        ResponseEntity<String> responseError = ResponseErrorGenerator
                .validateResultForKeyNotFoundOrWrongTypeErrors(result);
        if (responseError != null) {
            return responseError;
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/hget")
    public ResponseEntity<String> hget(@RequestBody @Valid SetHgetDto setHgetDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseErrorGenerator.generateErrorResponseByBinding(bindingResult);
        }
        String result = setService.getFieldByKeySet(setHgetDto);
        ResponseEntity<String> responseError = ResponseErrorGenerator
                .validateResultForKeyNotFoundOrWrongTypeErrors(result);
        if (responseError != null) {
            return responseError;
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
