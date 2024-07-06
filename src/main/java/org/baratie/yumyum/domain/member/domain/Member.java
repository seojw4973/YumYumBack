package org.baratie.yumyum.domain.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.baratie.yumyum.domain.BaseTimeEntity;
import org.baratie.yumyum.domain.member.dto.UpdateMemberDto;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type")
    private SocialType socialType;

    /**
     * 회원 정보 수정
     */
    public Member updateInfo(UpdateMemberDto updateMemberDto) {

        if (updateMemberDto.getProfileImage() != null && !updateMemberDto.getProfileImage().isBlank()) {
            this.imageUrl = updateMemberDto.getProfileImage();
        }

        if (updateMemberDto.getNickname() != null && !updateMemberDto.getNickname().isBlank()) {
            this.nickname = updateMemberDto.getNickname();
        }

        if (updateMemberDto.getPassword() != null && !updateMemberDto.getPassword().isBlank()) {
            this.password = updateMemberDto.getPassword();
        }

        if(updateMemberDto.getPhoneNumber() != null && !updateMemberDto.getPhoneNumber().isBlank()) {
            this.phoneNumber = updateMemberDto.getPhoneNumber();
        }

        return this;
    }
}
