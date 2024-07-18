package org.baratie.yumyum.domain.reply.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateRelyDto {

    @NotBlank
    private String content;
}
