package org.baratie.yumyum.domain.hashtag.dto;

import lombok.*;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HashtagDto {

    private String content;

    public Hashtag toEntity(){
        return Hashtag.builder()
                .content(content)
                .build();
    }
}
