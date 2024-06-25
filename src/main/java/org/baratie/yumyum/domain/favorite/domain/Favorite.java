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

    /**
     * 즐겨찾기 된 상태에서 누르면 즐겨찾기 취소 이므로 false 가 넘어온다.
     * @param isFavorite 클라이언트에서 보낸 즐겨찾기 변경 상태
     */
    public void changeFavoriteStatus(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public static Favorite insertFavorite(Member member, Store store) {
        return Favorite.builder()
                .member(member)
                .store(store)
                .isFavorite(true)
                .build();
    }
}
