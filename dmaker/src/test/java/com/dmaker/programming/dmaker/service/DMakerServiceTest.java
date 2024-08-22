package com.dmaker.programming.dmaker.service;

import com.dmaker.programming.dmaker.code.StatusCode;
import com.dmaker.programming.dmaker.dto.CreateDeveloper;
import com.dmaker.programming.dmaker.dto.DeveloperDetailDto;
import com.dmaker.programming.dmaker.dto.DeveloperDto;
import com.dmaker.programming.dmaker.entity.Developer;
import com.dmaker.programming.dmaker.entity.RetiredDeveloper;
import com.dmaker.programming.dmaker.exception.DMakerErrorCode;
import com.dmaker.programming.dmaker.exception.DMakerException;
import com.dmaker.programming.dmaker.repository.DeveloperRepository;
import com.dmaker.programming.dmaker.repository.RetiredDeveloperRepository;
import com.dmaker.programming.dmaker.type.DeveloperLevel;
import com.dmaker.programming.dmaker.type.DeveloperSkill;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.dmaker.programming.dmaker.code.StatusCode.EMPLOYED;
import static com.dmaker.programming.dmaker.type.DeveloperLevel.JUNIOR;
import static com.dmaker.programming.dmaker.type.DeveloperLevel.SENIOR;
import static com.dmaker.programming.dmaker.type.DeveloperSkill.FRONT_END;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {
    @Mock
    private DeveloperRepository developerRepository;



    @InjectMocks
    private DMakerService dMakerService;

    private final Developer defaultDeveloper = Developer.builder()
                .developerLevel(SENIOR)
                .developerSkill(FRONT_END)
                .experienceYears(12)
                .statusCode(EMPLOYED)
                .name("name")
                .age(12)
                .build();

    private final CreateDeveloper.Request defaultCreateRequest = CreateDeveloper.Request.builder()
            .developerLevel(SENIOR)
            .developerSkill(FRONT_END)
            .experienceYear(12)
            .memberId("memberId")
            .name("name")
            .age(32)
            .build();

    @Test
    public void testSomething(){

        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));


        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

        assertEquals(SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(FRONT_END, developerDetail.getDeveloperSkill());
        assertEquals(12, developerDetail.getExperienceYears());

    }

    @Test
    void createDeveloperTest_success(){
        // given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        given(developerRepository.save(any()))
                .willReturn(defaultDeveloper);
        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        // when
        dMakerService.createDeveloper(defaultCreateRequest);

        // then
        verify(developerRepository, times(1))
                .save(captor.capture());
        Developer savedDeveloper = captor.getValue();
        assertEquals(SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(FRONT_END, savedDeveloper.getDeveloperSkill());
        assertEquals(12, savedDeveloper.getExperienceYears());
    }

    @Test
    void createDeveloperTest_failed_with_duplicated(){
        // given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        // when
        // then
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(defaultCreateRequest));

        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }


}

