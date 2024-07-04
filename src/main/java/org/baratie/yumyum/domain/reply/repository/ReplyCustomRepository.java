package org.baratie.yumyum.domain.reply.repository;

import org.baratie.yumyum.domain.reply.dto.ReplyResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyCustomRepository {
    Long findReviewByReplyId(Long replyId);

    Slice<ReplyResponseDto> getReplyOnReview(Long reviewId, Pageable pageable);

}
