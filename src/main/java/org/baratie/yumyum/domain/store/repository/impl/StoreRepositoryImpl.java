package org.baratie.yumyum.domain.store.repository.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.*;
import org.baratie.yumyum.domain.store.repository.StoreCustomRepository;
import org.baratie.yumyum.global.subquery.TotalAvgGrade;
import org.baratie.yumyum.global.subquery.TotalReviewCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.*;
import static org.baratie.yumyum.domain.favorite.domain.QFavorite.favorite;
import static org.baratie.yumyum.domain.category.domain.QCategory.category;
import static org.baratie.yumyum.global.subquery.TotalAvgGrade.getAvgGradeWithStore;
import static org.baratie.yumyum.global.subquery.TotalReviewCount.getReviewTotalCountWithStore;

@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreCustomRepository {

    private final JPAQueryFactory query;

    /**
     * 가게가 존재하는지 + 가게가 삭제됐는지
     * @param storeId 검증이 필요한 가게 ID
     * @return 조회된 가게
     */
    @Override
    public Optional<Store> existAndDeletedStore(Long storeId) {
        Store findStore = query.selectFrom(store)
                .where(storeIdEq(storeId).and(store.isClosed.isFalse()))
                .fetchOne();

        return Optional.ofNullable(findStore);
    }

    /**
     * 가게 상세페이지
     * @param memberId 즐겨찾기 상태값 조회용
     * @param storeId 특정 가게 조회죵
     */
    @Override
    public StoreDetailDto findStoreDetail(Long memberId, Long storeId) {
        JPQLQuery<Boolean> favoriteStatus = JPAExpressions.select(favorite.isFavorite)
                .from(favorite)
                .where(favorite.store.id.eq(storeId), favorite.member.id.eq(memberId));

        return query.select(Projections.constructor(StoreDetailDto.class,
                        store.id,
                        store.name,
                        store.address,
                        store.hours,
                        store.call,
                        store.views,
                        Expressions.numberTemplate(Double.class, "round({0}, 2)", review.grade.avg().coalesce(0.0)).as("avgGrade"),
                        store.latitude,
                        store.longitude,
                        review.id.countDistinct(),
                        favorite.id.countDistinct(),
                        favoriteStatus))
                .from(store)
                .leftJoin(store.reviewList, review)
                .leftJoin(store.favoriteList, favorite)
                .where(storeIdEq(storeId))
                .fetchOne();
    }

    /**
     * 즐겨찾기한 맛집
     */
    @Override
    public Slice<MyFavoriteStoreDto> findFavoriteStore(Long memberId, Map<Long, List<String>> hashtagMap, Map<Long, String> imageMap, Pageable pageable) {

        JPQLQuery<Long> myFavoriteCount = JPAExpressions
                .select(favorite.count())
                .from(favorite)
                .where(favorite.isFavorite.eq(true), favorite.member.id.eq(memberId));

        List<MyFavoriteStoreDto> results = query.select(
                        Projections.constructor(MyFavoriteStoreDto.class,
                                store.id,
                                store.name,
                                store.address,
                                store.views,
                                ExpressionUtils.as(getAvgGradeWithStore(), "avgGrade"),
                                ExpressionUtils.as(getReviewTotalCountWithStore(), "totalReviewCount"),
                                ExpressionUtils.as(myFavoriteCount, "myFavoriteCount"),
                                favorite.countDistinct().as("storeFavoriteCount"),
                                favorite.isFavorite,
                                category.name
                        ))
                .from(store)
                .leftJoin(store.reviewList, review)
                .leftJoin(store.favoriteList, favorite)
                .leftJoin(store.categoryList, category)
                .where(memberIdEq(memberId), favoriteEq(true))
                .groupBy(store.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

            results.forEach(dto -> {
                List<String> hashtagList = hashtagMap.get(dto.getStoreId());
                dto.addHashtagList(hashtagList);
            });

            results.forEach(dto -> {
                String image = imageMap.get(dto.getStoreId());
                dto.addImage(image);
            });

        boolean hasNext = results.size() > pageable.getPageSize();

        if (hasNext) {
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    /**
     * 관리자 페이지 맛집 조회
     */
    @Override
    public Page<AdminStoreDto> getSimpleStore(Pageable pageable) {
        List<AdminStoreDto> results = query.select(Projections.constructor(AdminStoreDto.class,
                store.id,
                store.name,
                store.call,
                store.address,
                store.isClosed))
                .from(store)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(store.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = query.select(store.count()).from(store);

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanExpression storeIdEq(Long storeId) {
        return store.id.eq(storeId);
    }

    private BooleanExpression memberIdEq(Long memberId) { return favorite.member.id.eq(memberId);}

    private BooleanExpression favoriteEq(boolean status) { return favorite.isFavorite.eq(status);}

}
