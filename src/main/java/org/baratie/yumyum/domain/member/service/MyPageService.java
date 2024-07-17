package org.baratie.yumyum.domain.member.service;

import com.querydsl.core.group.Group;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.hashtag.repository.HashtagRepository;
import org.baratie.yumyum.domain.member.dto.MyReplyDto;
import org.baratie.yumyum.domain.reply.repository.ReplyRepository;
import org.baratie.yumyum.domain.review.dto.LikeReviewDto;
import org.baratie.yumyum.domain.review.dto.MyReviewDto;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.domain.store.dto.MyFavoriteStoreDto;
import org.baratie.yumyum.domain.store.repository.StoreRepository;
import org.baratie.yumyum.global.utils.file.repository.ImageRepository;
import org.baratie.yumyum.global.utils.pageDto.CustomSliceDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final ReplyRepository replyRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;

    /**
     * 내가 쓴 댓글 보기
     * @param memberId
     * @param pageable
     * @return Slice객체로 내가 쓴 댓글 리턴
     */
    public CustomSliceDto getMyReply(Long memberId, Pageable pageable) {
        Slice<MyReplyDto> myReply = replyRepository.findByMemberId(memberId, pageable);

        return new CustomSliceDto(myReply);
    }

    /**
     * 좋아요한 리뷰 조회
     * @param memberId
     * @param pageable
     * @return 좋아요한 리뷰 리스트 리턴
     */
    public CustomSliceDto getMyLikeReview(Long memberId, Pageable pageable) {

        Map<Long, List<String>> imageMap = imageRepository.findImageByReviewIdList();
        Slice<LikeReviewDto> likeReviews = reviewRepository.findLikeReviewsByMemberId(memberId, imageMap, pageable);
        return new CustomSliceDto(likeReviews);
    }

    /**
     * 내가 쓴 리뷰
     */
    public CustomSliceDto getMyReview(Long memberId, Pageable pageable) {

        Map<Long, List<String>> imageMap = imageRepository.findImageByReviewIdList();

        Slice<MyReviewDto> myReview = reviewRepository.getMyReview(memberId, imageMap, pageable);
        return new CustomSliceDto(myReview);
    }

    /**
     * 내가 즐겨찾기 한 맛집 조회
     * @param memberId
     */
    public CustomSliceDto getMyFavoriteStore(Long memberId, Pageable pageable) {

        Map<Long, List<String>> hashtagMap = hashtagRepository.findHashtagByStoreId();
        Map<Long, String> imageMap = imageRepository.findImageByStoreIdList();

        Slice<MyFavoriteStoreDto> favoriteStore = storeRepository.findFavoriteStore(memberId,hashtagMap, imageMap, pageable);
        return new CustomSliceDto(favoriteStore);
    }
}
