package org.baratie.yumyum.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class MemberBasicDto {

    private Long memberId;
    private String profileImage;
    private String nickname;
    private Long totalReviewCount;
    private double avgGrade;

    @Builder
    public MemberBasicDto(Long memberId, String profileImage, String nickname, Long totalReviewCount, double avgGrade) {
        this.memberId = memberId;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.totalReviewCount = totalReviewCount;
        this.avgGrade = (Math.round(avgGrade * 10.0) / 10.0);
    }
}
