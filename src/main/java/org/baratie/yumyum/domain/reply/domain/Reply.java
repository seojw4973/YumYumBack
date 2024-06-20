package org.baratie.yumyum.domain.reply.domain;

import jakarta.persistence.*;
import lombok.*;
import org.baratie.yumyum.domain.BaseTimeEntity;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.review.domain.Review;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Reply extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}
