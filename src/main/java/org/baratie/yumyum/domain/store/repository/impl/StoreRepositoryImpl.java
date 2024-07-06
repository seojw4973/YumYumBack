package org.baratie.yumyum.domain.store.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.AdminStoreDto;
import org.baratie.yumyum.domain.store.dto.MainStoreDto;
import org.baratie.yumyum.domain.store.dto.MyFavoriteStoreDto;
import org.baratie.yumyum.domain.store.dto.StoreDetailDto;
import org.baratie.yumyum.domain.store.repository.StoreCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

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
    public List<MainStoreDto> findTop10(String local) {
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

    @Override
    public List<MainStoreDto> findTop10OnFavorite(String local) {
        // 메인 쿼리 작성
        return query.select(Projections.constructor(MainStoreDto.class,
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
                .groupBy(store.id)  // 그룹화 필드 지정
                .orderBy(favorite.count().desc())  // 정렬 조건 추가
                .limit(10L)
                .fetch();

    }

    @Override
    public StoreDetailDto findStoreDetail(Long storeId) {

        return query
                .select(Projections.constructor(StoreDetailDto.class,
                        store.id,
                        store.name,
                        store.address,
                        store.hours,
                        store.call,
                        store.views,
                        review.grade.avg().as("avgGrade"),
                        store.latitude,
                        store.longitude,
                        review.id.countDistinct(),
                        favorite.id.countDistinct()))
                .from(store)
                .leftJoin(review).on(review.store.id.eq(store.id))
                .leftJoin(favorite).on(favorite.store.id.eq(store.id))
                .where(reviewIdEq(storeId))
                .fetchOne();
    }

    @Override
    public Slice<MyFavoriteStoreDto> findFavoriteStore(Long memberId, Pageable pageable) {

        JPQLQuery<Long> favoriteCount = JPAExpressions
                .select(favorite.count())
                .from(favorite)
                .where(favorite.store.id.eq(store.id));

        JPQLQuery<Long> totalReviewCount = JPAExpressions
                .select(review.count())
                .from(review)
                .where(review.store.id.eq(store.id));

        List<MyFavoriteStoreDto> results = query.select(Projections.constructor(MyFavoriteStoreDto.class,
                        image.imageUrl,
                        store.name,
                        store.hours,
                        store.address,
                        store.call,
                        totalReviewCount,
                        favoriteCount,
                        favorite.isFavorite
                ))
                .from(favorite)
                .leftJoin(favorite.store, store)
                .leftJoin(store.imageList, image)
                .groupBy(store.name)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .where(memberIdEq(memberId), favoriteEq(true))
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();

        if (hasNext) {
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    /**
     * 가게가 있는지 확인
     * @param storeId 조회할 가게 id
     * @return
     */
    private BooleanExpression storeIdEq(Long storeId) {
        return store.id.eq(storeId);
    }

    private BooleanExpression reviewIdEq(Long storeId) { return review.store.id.eq(storeId);}

    private BooleanExpression memberIdEq(Long memberId) { return favorite.member.id.eq(memberId);}

    private BooleanExpression favoriteEq(boolean status) { return favorite.isFavorite.eq(status);}

}
