package org.baratie.yumyum.domain.reply.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.reply.domain.Reply;
import org.baratie.yumyum.domain.review.domain.Review;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReplyDto {

    @NotBlank
    private String content;

    @NotNull
    private Long reviewId;

    public Reply toEntity(Review review, Member member) {
        return Reply.builder()
                .content(content)
                .review(review)
                .member(member)
                .build();
    }
}
