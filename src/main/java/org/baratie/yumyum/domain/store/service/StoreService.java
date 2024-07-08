package org.baratie.yumyum.domain.store.service;

import com.google.maps.errors.ApiException;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.favorite.repository.FavoriteRepository;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.hashtag.dto.HashtagDto;
import org.baratie.yumyum.domain.hashtag.repository.HashtagRepository;
import org.baratie.yumyum.domain.image.domain.Image;
import org.baratie.yumyum.domain.image.dto.ImageDto;
import org.baratie.yumyum.domain.image.repository.ImageRepository;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.baratie.yumyum.domain.menu.dto.MenuDto;
import org.baratie.yumyum.domain.menu.repository.MenuRepository;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.*;
import org.baratie.yumyum.domain.store.exception.StoreExistException;
import org.baratie.yumyum.domain.store.exception.StoreNotFoundException;
import org.baratie.yumyum.domain.store.repository.StoreRepository;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

        List<Menu> menuList = store.getMenuList();
        menuList.forEach(menu -> menu.addStore(store));

        List<Hashtag> hashtagList = store.getHashtagList();
        hashtagList.forEach(hashtag -> hashtag.addStore(store));

        List<Image> imageList = store.getImageList();
        imageList.forEach(image -> image.addStore(store));

        storeRepository.save(store);
    }

    /**
     * 내가 즐겨찾기 한 맛집 조회
     * @param memberId
     */
    public Slice<MyFavoriteStoreDto> getMyFavoriteStore(Long memberId, Pageable pageable) {
        return storeRepository.findFavoriteStore(memberId, pageable);
    }

    /**
     * 맛집 상세 조회
     * @param storeId 가게 pk
     * @return StoreDetailDto
     */
    @Transactional
    public StoreDetailDto StoreDetail(Long memberId, Long storeId){
        Store store = validationStoreId(storeId);
        store.incrementViews();
        storeRepository.save(store);

        StoreDetailDto storeDetailDto = storeRepository.findStoreDetail(memberId, storeId);
        List<String> images = imageRepository.findByStoreId(storeId);
        List<Hashtag> hashtags = hashtagRepository.findByStoreId(storeId);
        List<Menu> menus = menuRepository.findByStoreId(storeId);

        return storeDetailDto.tranceDto(storeDetailDto, hashtags, menus, images);
    }

    /**
     * 맛집 정보 수정
     * @param storeId
     * @param request
     */
    @Transactional
    public void updateStore(Long storeId, UpdateStoreDto request) {
        Store findstore = validationStoreId(storeId);
        Store updatedStore = findstore.updateStore(request);
        System.out.println(updatedStore);

        storeRepository.save(updatedStore);

        List<Menu> menuList = updatedStore.getMenuList();
        menuList.forEach(menu -> menu.addStore(updatedStore));
        menuRepository.saveAll(menuList);

        List<Hashtag> hashtagList = updatedStore.getHashtagList();
        hashtagList.forEach(hashtag -> hashtag.addStore(updatedStore));
        hashtagRepository.saveAll(hashtagList);

        List<Image> imageList = updatedStore.getImageList();
        imageList.forEach(image -> image.addStore(updatedStore));
        imageRepository.saveAll(imageList);
    }

    public List<MainStoreDto> getTop10OnFavorite(String local) {
        return storeRepository.findTop10OnFavorite(local);
    }

    public List<MainStoreDto> getTop10OnViews(String local) {
        return storeRepository.findTop10OnViews(local);
    }

    /**
     * 지역에 따른 Top10 가게 조회
     * @param local
     * @return 해당 지역에 조건에 맞는 Top10 가게 리턴
     */
    public List<MainStoreDto> getTop10(String local) {
        return storeRepository.findTop10(local);
    }

    /**
     * 관리자 페이지 맛집 전체 조회
     * @param pageable
     * @return 맛집 전체 데이터 Page 정보와 함께 리턴
     */
    public Page<AdminStoreDto> getAdminStores(Pageable pageable) {
        Page<Store> pageStore = storeRepository.findAll(pageable);
        return pageStore.map(m -> new AdminStoreDto(m.getId(), m.getName(), m.getCall(), m.getAddress(), m.isClosed()));
    }

    /**
     * 검색 시 맛집 리스트 조회
     * @param memberId 로그인한 유저 id값
     * @param keyword 검색어
     * @return 검색 조건에 맞는 맛집 리스트 30개 제한으로 출력
     */
    public List<SearchStoreDto> getSearchStores(Long memberId, String keyword) {
        return storeRepository.findSearchStore(memberId, keyword);
    }

    public List<SearchStoreDto> getNearByStore(Double lng, Double lat) {
        return storeRepository.findNearByStore(lng, lat);
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
