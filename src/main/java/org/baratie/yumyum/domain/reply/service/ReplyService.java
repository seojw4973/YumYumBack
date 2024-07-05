package org.baratie.yumyum.domain.reply.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.dto.MyReplyDto;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.reply.domain.Reply;
import org.baratie.yumyum.domain.reply.dto.CreateReplyDto;
import org.baratie.yumyum.domain.reply.dto.ReplyResponseDto;
import org.baratie.yumyum.domain.reply.dto.UpdateRelyDto;
import org.baratie.yumyum.domain.reply.exception.ReplyNotFoundException;
import org.baratie.yumyum.domain.reply.repository.ReplyRepository;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;
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
     * @param member
     * @param review
     * @param request
     */
    public void createReply(Member member, Review review, CreateReplyDto request){
        Reply reply = request.toEntity(review, member);

        replyRepository.save(reply);
    }

    /**
     * 리뷰에 달린 댓글 조회
     * @param reviewId
     * @param pageable
     * @return 리뷰에 달린 댓글
     */
    public Slice<ReplyResponseDto> getReplyOnReview(Long reviewId, Pageable pageable) {
        return replyRepository.getReplyOnReview(reviewId, pageable);
    }

    /**
     * 내가 쓴 댓글 보기
     * @param memberId
     * @param pageable
     * @return Slice객체로 내가 쓴 댓글 리턴
     */
    public Slice<MyReplyDto> getMyReply(Long memberId, Pageable pageable) {
        return replyRepository.findByMemberId(memberId, pageable);
    }

    /**
     * 댓글 id로 리뷰 id 가져오기
     * @param replyId
     * @return 댓글이 포함된 리뷰 id
     */
    private Long getReviewId(Long replyId) {
        return replyRepository.findReviewByReplyId(replyId);
    }

    /**
     * 댓글 수정
     * @param replyId 수정할 댓글 내용
     * @param request
     */
    public void updateReply(Long replyId, UpdateRelyDto request){
        Reply findReply = getReply(replyId);
        Reply updateReply = findReply.updateReview(request);
        replyRepository.save(updateReply);
    }

    /**
     * 댓글 삭제
     * @param replyId 삭제할 댓글 id
     */
    public void deleteReply(Long replyId){
        validationReplyId(replyId);
        replyRepository.deleteById(replyId);
    }

    /**
     * id 값에 해당하는 댓글 가져오기
     * @param replyId
     * @return
     */
    public Reply getReply(Long replyId){
        return replyRepository.findById(replyId).orElseThrow(
                () -> new ReplyNotFoundException(ErrorCode.REPLY_NOT_FOUND)
        );
    }

    /**
     * 댓글 존재 여부 확인
     *
     * @param replyId
     */
    public void validationReplyId(Long replyId){
        boolean existReply = reviewRepository.existsById(replyId);

        if(!existReply){
            throw new ReplyNotFoundException(ErrorCode.REPLY_NOT_FOUND);
        }
    }
}
