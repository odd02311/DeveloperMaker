package com.dmaker.programming.dmaker.controller;

import com.dmaker.programming.dmaker.dto.*;
import com.dmaker.programming.dmaker.exception.DMakerException;
import com.dmaker.programming.dmaker.service.DMakerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {

        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("/developers/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(
            @PathVariable final String memberId
    ) {

        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDevelopers(
            @Valid
            @RequestBody
            final CreateDeveloper.Request request
            ) {
        log.info("request : {}", request);

        return dMakerService.createDeveloper(request);
    }

    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDeveloper(
            @PathVariable final String memberId,
            @Valid
            @RequestBody
            final EditDeveloper.Request request
    ) {

        return dMakerService.editDeveloper(memberId, request);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDetailDto deleteDeveloper(
            final @PathVariable String memberId
    ){
        return dMakerService.deleteDeveloper(memberId);
    }




}


/*

    @RestController: DMakerController를 restcontroller라는 타입의 빈으로 등록
    Controller인데 @responsebody를 자동으로 달려있는 == @RestController이다.



    ============================================================================

    DmakerController(Bean)    DmakerService(Bean)   DeveloperRepository(Bean)

    Spring application context위에 3개를 만들어줬다.

    controller가 service를 가져다 쓰고 싶을땐
    private final DmakerService dMakerService 와 @RequiredArgsConstructor으로 자동으로 주입 해주고 사용하면 된다.

 */