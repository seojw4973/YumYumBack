package org.baratie.yumyum.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.likes.repository.LikesRepository;
import org.baratie.yumyum.domain.reply.repository.ReplyRepository;
import org.baratie.yumyum.global.utils.file.domain.ImageType;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.review.dto.*;
import org.baratie.yumyum.domain.member.exception.MemberIdNotEqualException;
import org.baratie.yumyum.domain.review.exception.ReviewNotFoundException;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.utils.file.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreService storeService;
    private final ImageService imageService;
    private final ReplyRepository replyRepository;
    private final LikesRepository likesRepository;

    /**
     * 리뷰 등록
     * @param member
     * @param request
     * 멤버 id 값이 필요한지는 의문, 토론 필요
     */
    @Transactional
    public void createReview(Member member, CreateReviewDto request, List<MultipartFile> files){
        Store store = storeService.validationStoreId(request.getStoreId());

        Review review = request.toEntity(store, member);
        Review saveReview = reviewRepository.save(review);

        if(files != null && !files.isEmpty()){
            imageService.fileUploadMultiple(ImageType.REVIEW, saveReview, files);
        }

    }

    /**
     * 리뷰 수정
     * @param reviewId 수정할 리뷰
     * @param request 수정 내용
     */
    @Transactional
    public void updateReview(Long memberId, Long reviewId, UpdateReviewRequestDto request, List<MultipartFile> files) {
        Review findReview = getReview(reviewId);

        isLoginMember(memberId, reviewId);

        Review updateReview = findReview.updateReview(request);

        if(files == null){
            imageService.targetFilesDelete(ImageType.REVIEW, updateReview.getId());
        }else if(!files.isEmpty()){
            imageService.targetFilesDelete(ImageType.REVIEW, updateReview.getId());
            imageService.fileUploadMultiple(ImageType.REVIEW, updateReview, files);
        }

        reviewRepository.save(updateReview);
    }

    /**
     * 리뷰 삭제
     * @param reviewId 삭제할 리뷰
     */
    @Transactional
    public void deleteReview(Long memberId, Long reviewId) {
        validationReviewId(reviewId);
        isLoginMember(memberId, reviewId);
        imageService.targetFilesDelete(ImageType.REVIEW, reviewId);
        replyRepository.deleteByReviewId(reviewId);
        likesRepository.deleteByReviewId(reviewId);
        reviewRepository.deleteById(reviewId);
    }

    /**
     * 리뷰가 존재하는지 확인
     *
     * @param reviewId 검증할 reviewId
     */
    public void validationReviewId(Long reviewId) {

        boolean exists = reviewRepository.existsById(reviewId);

        if (!exists) {
            throw new ReviewNotFoundException(ErrorCode.REVIEW_NOT_FOUND);
        }

    }

    /**
     * 로그인한 멤버인지 확인
     */
    private void isLoginMember(Long memberId, Long reviewId) {
        if (!reviewRepository.findMemberIdByReviewId(reviewId).equals(memberId)) {
            throw new MemberIdNotEqualException(ErrorCode.MEMBER_NOT_EQUAL);
        }
    }

    /**
     * @param reviewId 리뷰 객체를 받아오기 위한 id
     * @return Review
     */
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException(ErrorCode.REVIEW_NOT_FOUND)
        );
    }

}
