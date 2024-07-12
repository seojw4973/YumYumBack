package org.baratie.yumyum.domain.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyResponseDto {

    private Long replyId;
    private String imageUrl;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

}
