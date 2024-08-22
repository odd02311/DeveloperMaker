package com.dmaker.programming.dmaker.dto;

import com.dmaker.programming.dmaker.entity.Developer;
import com.dmaker.programming.dmaker.type.DeveloperLevel;
import com.dmaker.programming.dmaker.type.DeveloperSkill;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DeveloperDto {
    private DeveloperLevel developerLevel;
    private DeveloperSkill developerSkill;
    private String memberId;

    public static DeveloperDto fromEntity(Developer developer) {

        return DeveloperDto.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkill(developer.getDeveloperSkill())
                .memberId(developer.getMemberId())
                .build();
    }

}
