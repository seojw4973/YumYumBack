package org.baratie.yumyum.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
