package org.baratie.yumyum.domain.store.service;

import com.google.maps.errors.ApiException;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.hashtag.repository.HashtagRepository;
import org.baratie.yumyum.global.utils.file.domain.Image;
import org.baratie.yumyum.global.utils.file.domain.ImageType;
import org.baratie.yumyum.global.utils.file.repository.ImageRepository;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.baratie.yumyum.domain.menu.repository.MenuRepository;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.*;
import org.baratie.yumyum.domain.store.exception.StoreExistException;
import org.baratie.yumyum.domain.store.exception.StoreNotFoundException;
import org.baratie.yumyum.domain.store.repository.StoreRepository;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.utils.file.service.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final ReviewRepository reviewRepository;
    private final ImageService imageService;

    /**
     * 가게 등록
     * @param createStoreDto
     */
    @Transactional
    public void createStore(final CreateStoreDto createStoreDto, List<MultipartFile> files) throws IOException, InterruptedException, ApiException {
        existStoreName(createStoreDto.getName());

        BigDecimal[] bigDecimals = geoUtils.findGeoPoint(createStoreDto.getAddress());
        BigDecimal lat = bigDecimals[0];
        BigDecimal lng = bigDecimals[1];

        Store store = createStoreDto.toEntity(lat, lng);

        List<Menu> menuList = store.getMenuList();
        menuList.forEach(menu -> menu.addStore(store));

        List<Hashtag> hashtagList = store.getHashtagList();
        hashtagList.forEach(hashtag -> hashtag.addStore(store));

        if(files != null){
            imageService.fileUploadMultiple(ImageType.STORE, store, files);
        }

        storeRepository.save(store);
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
    public void updateStore(Long storeId, UpdateStoreDto request, List<MultipartFile> files) {
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

        if (files == null) {
            imageService.targetFilesDelete(ImageType.STORE, storeId);
        } else {
            imageService.targetFilesDelete(ImageType.STORE, storeId);
            imageService.fileUploadMultiple(ImageType.STORE, updatedStore, files);
        }
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

    /**
     * 가게 이름 중복 검사
     * @param storeName 검증할 가게 이름
     */
    private void existStoreName(String storeName){
       boolean existStore = storeRepository.existsByName(storeName);
       if(existStore){
        throw new StoreExistException(ErrorCode.EXIST_STORE_NAME);
       }
    }

}
