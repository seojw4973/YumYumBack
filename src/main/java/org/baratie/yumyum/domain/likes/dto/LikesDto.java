package org.baratie.yumyum.domain.likes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LikesDto {

    @NotBlank
    private Long reviewId;

    @NotBlank
    private Boolean status;

}
