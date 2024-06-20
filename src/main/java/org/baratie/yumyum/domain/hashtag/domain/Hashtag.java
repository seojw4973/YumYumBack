package org.baratie.yumyum.domain.hashtag.domain;

import jakarta.persistence.*;
import lombok.*;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.store.domain.Store;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Hashtag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "review")
    private Store store;
}
