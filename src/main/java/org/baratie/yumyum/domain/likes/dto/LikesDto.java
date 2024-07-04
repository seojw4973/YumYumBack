package org.baratie.yumyum.domain.likes.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LikesDto {
    private Long reviewId;
    private Boolean isLikes;

}
