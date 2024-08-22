package com.dmaker.programming.dmaker.service;

import com.dmaker.programming.dmaker.code.StatusCode;
import com.dmaker.programming.dmaker.dto.CreateDeveloper;
import com.dmaker.programming.dmaker.dto.DeveloperDetailDto;
import com.dmaker.programming.dmaker.dto.DeveloperDto;
import com.dmaker.programming.dmaker.dto.EditDeveloper;
import com.dmaker.programming.dmaker.entity.Developer;
import com.dmaker.programming.dmaker.entity.RetiredDeveloper;
import com.dmaker.programming.dmaker.repository.RetiredDeveloperRepository;
import com.dmaker.programming.dmaker.exception.DMakerException;
import com.dmaker.programming.dmaker.repository.DeveloperRepository;
import com.dmaker.programming.dmaker.type.DeveloperLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.dmaker.programming.dmaker.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository; // (1)
    private final RetiredDeveloperRepository retiredDeveloperRepository;
//    private final EntityManager em; // db 추상화

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
            validateCreateDeveloperRequest(request);



            // business logic start
             // (3)
            return CreateDeveloper.Response.fromEntity
                    (developerRepository.save(
                            createDeveloperFromRequest(request)
                    )
                    );
            // business logic end


    }

    private Developer createDeveloperFromRequest(CreateDeveloper.Request request){
        return  Developer.builder()   // (2)
                .developerLevel(request.getDeveloperLevel())
                .developerSkill(request.getDeveloperSkill())
                .experienceYears(request.getExperienceYear())
                .memberId(request.getMemberId())
                .statusCode(StatusCode.EMPLOYED)
                .name(request.getName())
                .age(request.getAge())
                .build();

    }

    private void validateCreateDeveloperRequest(
            @NonNull CreateDeveloper.Request request
    ) {
        //business validation
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYear());
//        if(developerLevel == DeveloperLevel.JUNIOR && experienceYear > 4){
//            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
//        }

        /*Optional<Developer> developer = */
        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));
        ;
//        if(developer.isPresent())
//            throw new DMakerException(DUPLICATED_MEMBER_ID);
    }

    @Transactional(readOnly = true)
    public List<DeveloperDto> getAllEmployedDevelopers() {

        return developerRepository.findDevelopersByStatusCodeEquals(
                StatusCode.EMPLOYED
                )
                .stream()
                .map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
    }

    @Transactional
    public DeveloperDetailDto editDeveloper(
            String memberId, EditDeveloper.Request request
    ) {

        validateDeveloperLevel(
                request.getDeveloperLevel(), request.getExperienceYear()
        );

        Developer developer = developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException(NO_DEVELOPER));

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkill(request.getDeveloperSkill());
        developer.setExperienceYears(request.getExperienceYear());

        return DeveloperDetailDto.fromEntity(developer);
    }





    private static void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYear) {
        if(developerLevel == DeveloperLevel.SENIOR
                && experienceYear < 10){
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel == DeveloperLevel.JUNIOR
                && (experienceYear < 4 || experienceYear > 10)){
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        // 1. EMPLOYED -> RETIRED
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(
                        () ->
                                new DMakerException(NO_DEVELOPER)
                );
        developer.setStatusCode(StatusCode.RETIRED);


        // 2. save into RetiredDeveloper
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .build();
        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDto.fromEntity(developer);

    }
}

/*

    비즈니스 로직 담당
    서비스 클래스 같은 경우 @requiredArgsConstructor 붙여주면 편리하다.
    (1) 자동으로 repository를 여기에 주입해준다.

    (2) developer entity를 builder, entity 패키지에있는 developer 클래스에 @Builder로 인해 사용 가능
        빌더를 통해서 각각의 데이터들을 하나씩 세팅 완료
        developer를 엔티티 객체를 하나 만들어서 developer repository에서 (3) save를 통해서 영속화,
        즉 db에 저장하는 로직을 만든것이다.

    이후 controller에 @service 빈을 주입해주어야 한다.

 */
