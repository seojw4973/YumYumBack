package org.baratie.yumyum.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baratie.yumyum.domain.reply.domain.Reply;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyReplyDto {
    private Long replyId;
    private String nickname;
    private String content;
    private Long totalReplyCount;
    private LocalDateTime createdAt;
    private Long reviewId;

    public MyReplyDto fromEntity(Reply reply){
        return MyReplyDto.builder()
                .replyId(reply.getId())
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt())
                .reviewId(reply.getReview().getId())
                .build();
    }
}
