package org.baratie.yumyum.domain.store.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.MainStoreDto;
import org.baratie.yumyum.domain.store.repository.StoreCustomRepository;

import java.util.List;
import java.util.Optional;

import static org.baratie.yumyum.domain.image.domain.QImage.image;
import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.*;
import static org.baratie.yumyum.domain.favorite.domain.QFavorite.favorite;

@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreCustomRepository {

    private final JPAQueryFactory query;

    /**
     * @param storeId 가게가 존재하는지 검증이 필요한 가게 ID
     * @return Optional<Store>
     */
    @Override
    public Optional<Store> existAndDeletedStore(Long storeId) {

        Store findStore = query.selectFrom(store)
                .where(storeIdEq(storeId).and(store.isClosed.isFalse()))
                .fetchOne();

        return Optional.ofNullable(findStore);
    }

    @Override
    public List<MainStoreDto> findTo10(String local) {
        // 메인 쿼리 작성
        List<MainStoreDto> results = query
                .select(Projections.constructor(MainStoreDto.class,
                        store.id,
                        store.name,
                        image.imageUrl,
                        review.grade.avg().as("avgGrade"),
                        review.countDistinct().as("reviewCount"),
                        favorite.countDistinct().as("favoriteCount")
                ))
                .from(store)
                .leftJoin(review).on(review.store.id.eq(store.id))
                .leftJoin(favorite).on(favorite.store.id.eq(store.id))
                .leftJoin(image).on(image.store.id.eq(store.id))
                .where(store.address.contains(local))
                .groupBy(store.id, image.imageUrl)  // 그룹화 필드 지정
                .orderBy(review.grade.avg().desc(), favorite.count().desc())  // 정렬 조건 추가
                .limit(10L)
                .fetch();

        return results;
    }

    /**
     * 가게가 있는지 확인
     * @param storeId 조회할 가게 id
     * @return
     */
    private BooleanExpression storeIdEq(Long storeId) {
        return store.id.eq(storeId);
    }
}
