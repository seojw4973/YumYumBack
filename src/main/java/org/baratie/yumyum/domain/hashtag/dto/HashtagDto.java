package org.baratie.yumyum.domain.hashtag.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HashtagDto {

    @NotBlank
    private String content;

    public Hashtag toEntity(){
        return Hashtag.builder()
                .content(content)
                .build();
    }
}
