package com.dmaker.programming.dmaker.dto;

import com.dmaker.programming.dmaker.entity.Developer;
import com.dmaker.programming.dmaker.type.DeveloperLevel;
import com.dmaker.programming.dmaker.type.DeveloperSkill;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

public class EditDeveloper {

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

//        @NotNull
//        @Size(min = 3, max =50, message= "memberId size must 3 to 50")
//        private String memberId; // 고유 키기 때문에 edit에선 제거

    }


}
