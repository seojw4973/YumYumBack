package org.baratie.yumyum.domain.likes.domain;

import jakarta.persistence.*;
import lombok.*;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.review.domain.Review;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Likes {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @Column(name = "is_likes")
    private boolean isLikes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static Likes insertLikes(Member member, Review review) {
        return Likes.builder().member(member).review(review).build();
    }

    public void changeReviewLikes(boolean isLikes){
        this.isLikes = isLikes;
    }
}
