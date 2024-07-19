package org.baratie.yumyum.domain.reply.repository;

import org.baratie.yumyum.domain.member.dto.MyReplyDto;
import org.baratie.yumyum.domain.reply.domain.Reply;
import org.baratie.yumyum.domain.reply.dto.ReplyResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyCustomRepository {
    Long findReviewByReplyId(Long replyId);

    Slice<ReplyResponseDto> getReplyOnReview(Long reviewId, Pageable pageable);

    Slice<MyReplyDto> findByMemberId(Long memberId, Pageable pageable);

    List<Long> findByReviewId(Long reviewId);

    Long checkReplyMember(Long memberId, Long replyId);

}
