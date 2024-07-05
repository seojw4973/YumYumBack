package org.baratie.yumyum.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.image.domain.Image;
import org.baratie.yumyum.domain.image.repository.ImageRepository;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.review.dto.ReviewAllDto;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;
import org.baratie.yumyum.domain.review.dto.UpdateReviewRequestDto;
import org.baratie.yumyum.domain.review.dto.CreateReviewDto;
import org.baratie.yumyum.domain.member.exception.MemberIdNotEqualException;
import org.baratie.yumyum.domain.review.exception.ReviewNotFoundException;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final MemberService memberService;
    private final StoreService storeService;

    /**
     * 리뷰 등록
     * @param customUserDetails
     * @param request
     * 멤버 id 값이 필요한지는 의문, 토론 필요
     */
    @Transactional
    public void createReview(CustomUserDetails customUserDetails, CreateReviewDto request){
        Member member = memberService.validationMemberId(customUserDetails.getId());
        Store store = storeService.validationStoreId(request.getStoreId());

        Review review = request.toEntity(store, member);
        Review saveReview = reviewRepository.save(review);

        List<Image> imageList = review.getImageList();
        imageList.forEach(image -> image.addReview(saveReview));
        imageRepository.saveAll(imageList);

    }

    /**
     * 리뷰 상세조회
     * @param reviewId
     * @return 리뷰 상세페이지 데이터
     */
    public ReviewDetailDto getReviewDetail(Long reviewId) {
        validationReviewId(reviewId);

        Long memberId = reviewRepository.findMemberIdByReviewId(reviewId);
        ReviewDetailDto reviewDetail = reviewRepository.findReviewDetail(memberId, reviewId);
        List<String> images = imageRepository.findByReviewId(reviewId);

        return reviewDetail.tranceDto(reviewDetail, images);
    }

    /**
     * 리뷰 수정
     * @param reviewId 수정할 리뷰
     * @param request 수정 내용
     */
    @Transactional
    public void updateReview(Long memberId, Long reviewId, UpdateReviewRequestDto request) {
        Review findReview = getReview(reviewId);

        isLoginMember(memberId, reviewId);

        Review updateReview = findReview.updateReview(request);

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

        reviewRepository.deleteById(reviewId);
    }

    /**
     * 리뷰 전체 조회 (이미지 제외)
     * @param pageable
     * @return
     */
    public Slice<ReviewAllDto> getAllReview(Pageable pageable){
        return reviewRepository.findAllReviews(pageable);
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
