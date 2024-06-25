package org.baratie.yumyum.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.favorite.repository.FavoriteRepository;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.hashtag.repository.HashtagRepository;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.baratie.yumyum.domain.menu.repository.MenuRepository;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.StoreDetailDto;
import org.baratie.yumyum.domain.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;
    private final HashtagRepository hashtagRepository;
    private final MenuRepository menuRepository;

    public StoreDetailDto StoreDetail(Long id){
        Store store = storeRepository.findById(id).orElseThrow();
        int reviewCount = reviewRepository.countReviewByStoreId(id);
        int favoriteCount = favoriteRepository.countFavoriteByStoreId(id);
        List<Hashtag> hashtags = hashtagRepository.findByStoreId(id);
        List<Menu> menuList = menuRepository.findByStoreId(id);

        StoreDetailDto storeDetailDto = new StoreDetailDto().builder()
                .name(store.getName())
                .address(store.getAddress())
                .hours(store.getHours())
                .views(store.getViews())
                .reviewCount(reviewCount)
                .favoriteCount(favoriteCount)
                .hashtagList(hashtags)
                .menuList(menuList)
                .build();
        return storeDetailDto;
    }

}
