package org.baratie.yumyum.domain.reply.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.exception.MemberIdNotEqualException;
import org.baratie.yumyum.domain.reply.domain.Reply;
import org.baratie.yumyum.domain.reply.dto.CreateReplyDto;
import org.baratie.yumyum.domain.reply.dto.ReplyResponseDto;
import org.baratie.yumyum.domain.reply.dto.UpdateRelyDto;
import org.baratie.yumyum.domain.reply.exception.ReplyNotFoundException;
import org.baratie.yumyum.domain.reply.repository.ReplyRepository;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.utils.pageDto.CustomSliceDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {

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
    public CustomSliceDto getReplyOnReview(Long reviewId, Pageable pageable) {
        Slice<ReplyResponseDto> replyOnReview = replyRepository.getReplyOnReview(reviewId, pageable);
        return new CustomSliceDto(replyOnReview);
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
    public void updateReply(Long memberId, Long replyId, UpdateRelyDto request) {
        validateReplyMemberSameLoginMember(memberId, replyId);


        Reply findReply = getReply(replyId);
        Reply updateReply = findReply.updateReview(request);
        replyRepository.save(updateReply);
    }

    /**
     * 댓글 삭제
     * @param replyId 삭제할 댓글 id
     */
    public void deleteReply(Long memberId, Long replyId){
        validationReplyId(replyId);
        validateReplyMemberSameLoginMember(memberId, replyId);

        replyRepository.deleteById(replyId);
    }

    /**
     * 댓글 작성자 본인인지 확인
     */
    private void validateReplyMemberSameLoginMember(Long memberId, Long replyId) {
        Long checked = replyRepository.checkReplyMember(memberId, replyId);

        if (checked <= 0) {
            throw new MemberIdNotEqualException(ErrorCode.MEMBER_NOT_EQUAL);
        }
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
        boolean existReply = replyRepository.existsById(replyId);

        if(!existReply){
            throw new ReplyNotFoundException(ErrorCode.REPLY_NOT_FOUND);
        }
    }
}
