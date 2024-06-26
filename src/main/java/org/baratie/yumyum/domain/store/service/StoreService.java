package org.baratie.yumyum.domain.store.service;

import com.google.maps.errors.ApiException;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.favorite.repository.FavoriteRepository;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.hashtag.repository.HashtagRepository;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.baratie.yumyum.domain.menu.repository.MenuRepository;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.CreateStoreDto;
import org.baratie.yumyum.domain.store.dto.StoreDetailDto;
import org.baratie.yumyum.domain.store.exception.StoreExistException;
import org.baratie.yumyum.domain.store.exception.StoreNotFoundException;
import org.baratie.yumyum.domain.store.repository.StoreRepository;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;
    private final MenuRepository menuRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional
    public void createStore(final CreateStoreDto createStoreDto) throws IOException, InterruptedException, ApiException {
        existStoreName(createStoreDto.getName());

        Store store = createStoreDto.toEntity();

        Store saveStore = storeRepository.save(store);

        List<Menu> menuList = store.getMenuList();
        menuList.forEach(menu -> menu.addStore(saveStore));

        menuRepository.saveAll(menuList);

        List<Hashtag> hashtagList = store.getHashtagList();
        hashtagList.forEach(hashtag -> hashtag.addStore(saveStore));

        hashtagRepository.saveAll(hashtagList);
    }

    /**
     * 맛집 상세 조회
     * @param storeId 가게 pk
     * @return StoreDetailDto
     */
    public StoreDetailDto StoreDetail(Long storeId){
        Store store = validationStoreId(storeId);

        int reviewCount = reviewRepository.countReviewByStoreId(storeId);
        int favoriteCount = favoriteRepository.countFavoriteByStoreId(storeId);

        return StoreDetailDto.fromEntity(store, reviewCount, favoriteCount);
    }


    /**
     * 가게 정보 가져오기
     * @param storeId 가게 pk
     * @return Store
     */
    private Store validationStoreId(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(
                () -> new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND)
        );
    }

    private void existStoreName(String storeName){
       boolean existStore = storeRepository.existsByName(storeName);
       if(existStore){
        throw new StoreExistException(ErrorCode.EXIST_STORE_NAME);
       }
    }

    /**
     * 주소값을 위도, 경도 좌표값으로 변환
     * @param address 주소값
     * @return latlng 해당 주소의 위도, 경도 좌표
     */
    public BigDecimal[] addressToLagLng(String address) throws IOException, InterruptedException, ApiException {
        System.out.println("address = " + address);
        BigDecimal[] latlng =  GeoUtils.findGeoPoint(address);
        return latlng;
    }

}
