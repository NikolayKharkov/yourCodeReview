package ru.kh.redis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kh.redis.services.ListService;
import ru.kh.redis.dto.keysDto.KeyDto;
import ru.kh.redis.dto.listsDto.ListEntityDto;
import ru.kh.redis.dto.listsDto.ListLGetDto;
import ru.kh.redis.dto.listsDto.ListLindexDto;
import ru.kh.redis.dto.listsDto.ListLsetDto;
import ru.kh.redis.utils.Consts;
import ru.kh.redis.utils.ResponseErrorGenerator;

import javax.validation.Valid;

@RestController
public class ListValuesController {

    private final ListService listService;

    @Autowired
    public ListValuesController(ListService listService) {
        this.listService = listService;
    }

    @PostMapping("/lpush")
    public ResponseEntity<String> lpush(@RequestBody @Valid ListEntityDto listEntityDto) {
        String result = listService.pushValuesOrCreate(listEntityDto);
        ResponseEntity<String> responseError = ResponseErrorGenerator
                .validateResultForKeyNotFoundOrWrongTypeErrors(result);
        if (responseError != null) {
            return responseError;
        }
        return new ResponseEntity<>("(integer) " + result, HttpStatus.OK);

    }

    @PostMapping("/lset")
    public ResponseEntity<String> lset(@RequestBody @Valid ListLsetDto listLsetDto) {
        String result = listService.replaceValueByIndex(listLsetDto);
        ResponseEntity<String> responseError = ResponseErrorGenerator
                .validateResultForKeyNotFoundOrWrongTypeErrors(result);
        if (responseError != null) {
            return responseError;
        }
        if (result.equals("-1")) {
            return ResponseErrorGenerator.generateErrorResponseWithMessage(Consts.ERROR_MESSAGE_INDEX_BOUND_OF_ARRAY);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/lget")
    public ResponseEntity<String> lget(@RequestBody @Valid ListLGetDto listLGetDto) {
        String result = listService.getElementsByStartAndFinishIndices(listLGetDto);
        ResponseEntity<String> responseError = ResponseErrorGenerator
                .validateResultForKeyNotFoundOrWrongTypeErrors(result);
        if (responseError != null) {
            return responseError;
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("lindex")
    public ResponseEntity<String> lindex(@RequestBody @Valid ListLindexDto listLindexDto) {
        String result = listService.getElementFromListByKey(listLindexDto);
        ResponseEntity<String> responseError = ResponseErrorGenerator
                .validateResultForKeyNotFoundOrWrongTypeErrors(result);
        if (responseError != null) {
            return responseError;
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/llen")
    public ResponseEntity<String> llen(@RequestBody @Valid KeyDto keyDto) {
        String result = listService.getListSize(keyDto);
        ResponseEntity<String> responseError = ResponseErrorGenerator
                .validateResultForKeyNotFoundOrWrongTypeErrors(result);
        if (responseError != null) {
            return responseError;
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
