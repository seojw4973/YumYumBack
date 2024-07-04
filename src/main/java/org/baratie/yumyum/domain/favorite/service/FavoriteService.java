package org.baratie.yumyum.domain.favorite.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.favorite.domain.Favorite;
import org.baratie.yumyum.domain.favorite.dto.FavoriteDto;
import org.baratie.yumyum.domain.favorite.repository.FavoriteRepository;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    /**
     * 즐겨찾기 추가 or 취소
     * @param member 인증된 사용자 객체
     * @param store 즐겨찾기 타겟
     * @param request 즐겨찾기 추가할 가게 id와 즐겨찾기 상태
     */
    public void addFavorite(Member member, Store store, FavoriteDto request) {

        favoriteRepository.exist(member.getId(), store.getId()).ifPresentOrElse(
                favorite -> {
                    favorite.changeFavoriteStatus(request.getIsFavorite());
                    favoriteRepository.save(favorite);
                },
                () -> favoriteRepository.save(Favorite.insertFavorite(member, store))
        );

    }
}
