package org.baratie.yumyum.domain.favorite.domain;

import jakarta.persistence.*;
import lombok.*;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.store.domain.Store;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Favorite {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
