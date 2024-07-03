package org.baratie.yumyum.domain.reply.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.reply.domain.Reply;
import org.baratie.yumyum.domain.reply.dto.CreateReplyDto;
import org.baratie.yumyum.domain.reply.dto.UpdateRelyDto;
import org.baratie.yumyum.domain.reply.exception.ReplyNotFoundException;
import org.baratie.yumyum.domain.reply.repository.ReplyRepository;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final MemberService memberService;
    private final ReviewRepository reviewRepository;
    private final ReplyRepository replyRepository;


    /**
     * 댓글 작성
     * @param customUserDetails
     * @param request
     */
    public void createReply(CustomUserDetails customUserDetails, CreateReplyDto request){
        Member member = memberService.validationMemberId(customUserDetails.getId());
        Optional<Review> review = reviewRepository.findById(request.getReviewId());

        Reply reply = request.toEntity(review.get(), member);
        replyRepository.save(reply);
    }

    /**
     * 댓글 수정
     * @param replyId
     * @param request
     */
    public void updateReply(Long replyId, UpdateRelyDto request){
        validateReply(replyId);

        Reply findReply = getReply(replyId);
        Reply updateReply = findReply.updateReview(request);
        replyRepository.save(updateReply);
    }

    public void deleteReply(Long replyId){
        validateReply(replyId);
        replyRepository.deleteById(replyId);
    }

    /**
     * id 값에 해당하는 댓글 가져오기
     * @param replyId
     * @return
     */
    public Reply getReply(Long replyId){
        return replyRepository.findById(replyId).get();
    }

    /**
     * 댓글 존재 여부 확인
     * @param replyId
     * @return
     */
    public boolean validateReply(Long replyId){
        boolean existReply = reviewRepository.existsById(replyId);
        if(!existReply){
            throw new ReplyNotFoundException(ErrorCode.REPLY_NOT_FOUND);
        }
        return true;
    }
}
