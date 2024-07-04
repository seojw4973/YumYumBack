package org.baratie.yumyum.domain.likes.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.likes.domain.Likes;
import org.baratie.yumyum.domain.likes.dto.LikesDto;
import org.baratie.yumyum.domain.likes.repository.LikesRepository;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final ReviewRepository reviewRepository;

    public void checkLikes(Member member, Review review,LikesDto likesDto){
        likesRepository.exist(member.getId(), review.getId()).ifPresentOrElse(
                likes -> {
                    likes.changeReviewLikes(likesDto.getIsLikes());
                    likesRepository.save(likes);
                },
                () -> likesRepository.save(Likes.insertLikes(member, review))
        );


    }

}
