package com.dmaker.programming.dmaker.dto;

import com.dmaker.programming.dmaker.entity.Developer;
import com.dmaker.programming.dmaker.type.DeveloperLevel;
import com.dmaker.programming.dmaker.type.DeveloperSkill;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

public class CreateDeveloper {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request{
        @NotNull
        private DeveloperLevel developerLevel;
        @NotNull
        private DeveloperSkill developerSkill;
        @NotNull
        @Min(0)
        @Max(20)
        private Integer experienceYear;

        @NotNull
        @Size(min = 3, max =50, message= "memberId size must 3 to 50")
        private String memberId;
        @NotNull
        @Size(min = 3, max =20, message= "name size must 3 to 20")
        private String name;
        @Min(18)
        private Integer age;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private DeveloperLevel developerLevel;
        private DeveloperSkill developerSkill;
        private Integer experienceYear;
        private String memberId;

        public static Response fromEntity(@NonNull Developer developer){
            return Response.builder()
                    .developerLevel(developer.getDeveloperLevel())
                    .developerSkill(developer.getDeveloperSkill())
                    .experienceYear(developer.getExperienceYears())
                    .memberId(developer.getMemberId())
                    .build();
        }

    }
}
