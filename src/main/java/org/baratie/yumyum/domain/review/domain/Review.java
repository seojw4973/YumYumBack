package org.baratie.yumyum.domain.review.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.baratie.yumyum.domain.BaseTimeEntity;
import org.baratie.yumyum.domain.reply.domain.Reply;
import org.baratie.yumyum.global.utils.file.domain.Image;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.review.dto.UpdateReviewRequestDto;
import org.baratie.yumyum.domain.store.domain.Store;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageList")
public class Review extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "grade", nullable = false)
    private double grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<Image> imageList = new ArrayList<>();

    /**
     * 리뷰 수정
     * @param request 변경할 리뷰 내용
     * @return Review
     */
    public Review updateReview(UpdateReviewRequestDto request) {
        this.content = request.getContent();
        this.grade = request.getGrade();

        return this;
    }
}
