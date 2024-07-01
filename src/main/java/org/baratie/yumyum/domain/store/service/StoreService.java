package org.baratie.yumyum.domain.store.service;

import com.google.maps.errors.ApiException;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.favorite.repository.FavoriteRepository;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.hashtag.repository.HashtagRepository;
import org.baratie.yumyum.domain.image.domain.Image;
import org.baratie.yumyum.domain.image.repository.ImageRepository;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.baratie.yumyum.domain.menu.repository.MenuRepository;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.CreateStoreDto;
import org.baratie.yumyum.domain.store.dto.StoreDetailDto;
import org.baratie.yumyum.domain.store.dto.UpdateStoreDto;
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
    private final GeoUtils geoUtils;
    private final ImageRepository imageRepository;

    /**
     * 가게 등록
     * @param createStoreDto
     */
    @Transactional
    public void createStore(final CreateStoreDto createStoreDto) throws IOException, InterruptedException, ApiException {
        existStoreName(createStoreDto.getName());

        BigDecimal[] bigDecimals = geoUtils.findGeoPoint(createStoreDto.getAddress());
        BigDecimal lat = bigDecimals[0];
        BigDecimal lng = bigDecimals[1];

        Store store = createStoreDto.toEntity(lat, lng);
        Store saveStore = storeRepository.save(store);

        List<Menu> menuList = store.getMenuList();
        menuList.forEach(menu -> menu.addStore(saveStore));
        menuRepository.saveAll(menuList);

        List<Hashtag> hashtagList = store.getHashtagList();
        hashtagList.forEach(hashtag -> hashtag.addStore(saveStore));
        hashtagRepository.saveAll(hashtagList);

        List<Image> imageList = store.getImageList();
        imageList.forEach(image -> image.addStore(saveStore));
        imageRepository.saveAll(imageList);
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

    @Transactional
    public void updateStore(Long storeId, UpdateStoreDto request) throws IOException, InterruptedException, ApiException {
        Store findstore = validationStoreId(storeId);
        Store updateStore = findstore.updateStore(request);

        if(!findstore.getAddress().equals(updateStore.getAddress())) {
            BigDecimal[] bigDecimals = geoUtils.findGeoPoint(request.getAddress());
            BigDecimal lat = bigDecimals[0];
            BigDecimal lng = bigDecimals[1];

            updateStore.builder()
                    .latitude(lat)
                    .longitude(lng)
                    .build();
        }
        Store saveStore = storeRepository.save(updateStore);
        List<Menu> menuList = updateStore.getMenuList();
        menuList.forEach(menu -> menu.addStore(saveStore));
        menuRepository.saveAll(menuList);

        List<Hashtag> hashtagList = updateStore.getHashtagList();
        hashtagList.forEach(hashtag -> hashtag.addStore(saveStore));
        hashtagRepository.saveAll(hashtagList);

        List<Image> imageList = updateStore.getImageList();
        imageList.forEach(image -> image.addStore(saveStore));
        imageRepository.saveAll(imageList);

    }


    /**
     * 가게가 존재하는지와 폐업한 가게인지 확인
     * @param storeId 가게 pk
     * @return Store
     */
    public Store validationStoreId(Long storeId) {
        return storeRepository.existAndDeletedStore(storeId).orElseThrow(
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
        BigDecimal[] latlng =  geoUtils.findGeoPoint(address);
        return latlng;
    }

}
