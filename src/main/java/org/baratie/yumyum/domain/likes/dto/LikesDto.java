package org.baratie.yumyum.domain.likes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LikesDto {

    @NotBlank
    private Long reviewId;

    @NotNull
    private Boolean status;

}
