package org.baratie.yumyum.domain.review.domain;

import jakarta.persistence.*;
import lombok.*;
import org.baratie.yumyum.domain.BaseTimeEntity;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.store.domain.Store;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Review extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "grade", nullable = false)
    private int grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
