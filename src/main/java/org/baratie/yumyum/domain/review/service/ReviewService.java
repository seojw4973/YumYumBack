package org.baratie.yumyum.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.global.utils.file.domain.ImageType;
import org.baratie.yumyum.global.utils.file.repository.ImageRepository;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.review.dto.LikeReviewDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final MemberService memberService;
    private final StoreService storeService;
    private final ImageService imageService;

    /**
     * 리뷰 등록
     * @param customUserDetails
     * @param request
     * 멤버 id 값이 필요한지는 의문, 토론 필요
     */
    @Transactional
    public void createReview(CustomUserDetails customUserDetails, CreateReviewDto request, List<MultipartFile> files){
        Member member = memberService.getMember(customUserDetails.getId());
        Store store = storeService.validationStoreId(request.getStoreId());

        Review review = request.toEntity(store, member);
        Review saveReview = reviewRepository.save(review);

        if(files != null){
            imageService.fileUploadMultiple(ImageType.REVIEW, saveReview, files);
        }

    }

    /**
     * 좋아요한 리뷰 조회
     * @param memberId
     * @param pageable
     * @return 좋아요한 리뷰 리스트 리턴
     */
    public Slice<LikeReviewDto> getMyLikeReview(Long memberId, Pageable pageable) {
        return reviewRepository.findLikeReviewsByMemberId(memberId, pageable);
    }

    /**
     * 내가 쓴 리뷰
     */
    public Slice<MyReviewDto> getMyReview(Long memberId, Pageable pageable) {
        return reviewRepository.getMyReview(memberId, pageable);
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
