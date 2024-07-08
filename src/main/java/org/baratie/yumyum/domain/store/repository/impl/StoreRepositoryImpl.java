package org.baratie.yumyum.domain.store.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.*;
import org.baratie.yumyum.domain.store.repository.StoreCustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.baratie.yumyum.domain.image.domain.QImage.image;
import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.*;
import static org.baratie.yumyum.domain.favorite.domain.QFavorite.favorite;
import static org.baratie.yumyum.domain.category.domain.QCategory.category;
import static org.baratie.yumyum.domain.hashtag.domain.QHashtag.*;

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
        return query
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
                .groupBy(store.id)  // 그룹화 필드 지정
                .orderBy(review.grade.avg().desc(), favorite.count().desc())  // 정렬 조건 추가
                .limit(10L)
                .fetch();
    }

    @Override
    public List<MainStoreDto> findTop10OnViews(String local) {
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
                .orderBy(store.views.desc())  // 정렬 조건 추가
                .limit(10L)
                .fetch();
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
    public StoreDetailDto findStoreDetail(Long memberId, Long storeId) {
        JPQLQuery<Boolean> favoriteStatus = JPAExpressions.select(favorite.isFavorite)
                .from(favorite)
                .where(favorite.store.id.eq(storeId), favorite.member.id.eq(memberId));

        return query
                .select(Projections.constructor(StoreDetailDto.class,
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
                .leftJoin(review).on(review.store.id.eq(store.id))
                .leftJoin(favorite).on(favorite.store.id.eq(store.id))
                .where(storeIdEq(storeId))
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

    @Override
    public List<SearchStoreDto> findSearchStore(Long memberId,String keyword) {
        JPQLQuery<Long> favoriteCount = JPAExpressions
                .select(favorite.count())
                .from(favorite)
                .where(favorite.store.id.eq(store.id).and(favorite.isFavorite.eq(true)));

        JPQLQuery<Long> totalReviewCount = JPAExpressions
                .select(review.count())
                .from(review)
                .where(review.store.id.eq(store.id));

        List<SearchStoreDto> searchStoreList = query.select(Projections.constructor(SearchStoreDto.class,
                store.id,
                store.name,
                image.imageUrl,
                store.address,
                store.views,
                        Expressions.numberTemplate(Double.class, "round({0}, 2)", review.grade.avg().coalesce(0.0)),
                totalReviewCount,
                favoriteCount,
                favorite.isFavorite,
                category.name
                ))
                .from(store)
                .leftJoin(favorite).on(favorite.store.id.eq(store.id).and(favorite.member.id.eq(memberId)))
                .leftJoin(review).on(review.store.id.eq(store.id))
                .leftJoin(category).on(category.store.id.eq(store.id))
                .leftJoin(image).on(image.store.id.eq(store.id))
                .leftJoin(hashtag).on(hashtag.store.id.eq(store.id))
                .where(nameContain(keyword))
                .groupBy(store.id, store.name, store.address, category.name)
                .limit(30L)
                .fetch();

        for (SearchStoreDto dto : searchStoreList) {
            List<String> hashtags = query.select(hashtag.content)
                    .from(hashtag)
                    .where(hashtag.store.id.eq(dto.getStoreId()))
                    .limit(3L)
                    .fetch();
            dto.addHashtagList(hashtags);
        }
        return searchStoreList;
    }

    @Override
    public List<SearchStoreDto> findNearByStore(Double lng, Double lat) {
        // Favorite count subquery
        JPQLQuery<Long> favoriteCount = JPAExpressions
                .select(favorite.count())
                .from(favorite)
                .where(favorite.store.id.eq(store.id).and(favorite.isFavorite.eq(true)));

        // Total review count subquery
        JPQLQuery<Long> totalReviewCount = JPAExpressions
                .select(review.count())
                .from(review)
                .where(review.store.id.eq(store.id));

        // Distance calculation expression
        NumberTemplate<Double> distanceExpr = Expressions.numberTemplate(Double.class,
                "ST_Distance_Sphere(Point({0}, {1}), Point({2}, {3}))",
                store.longitude, store.latitude, lng, lat);

        // Main query

        return query.select(Projections.constructor(SearchStoreDto.class,
                        store.id,
                        store.name,
                        image.imageUrl,
                        store.address,
                        store.views,
                        Expressions.numberTemplate(Double.class, "round({0}, 2)", review.grade.avg().coalesce(0.0)),
                        totalReviewCount,
                        favoriteCount,
                        favorite.isFavorite,
                        category.name))
                .from(store)
                .leftJoin(favorite).on(favorite.store.id.eq(store.id))
                .leftJoin(review).on(review.store.id.eq(store.id))
                .leftJoin(category).on(category.store.id.eq(store.id))
                .leftJoin(image).on(image.store.id.eq(store.id))
                .leftJoin(hashtag).on(hashtag.store.id.eq(store.id))
                .where(distanceExpr.loe(5000))  // Distance filter
                .groupBy(store.id, store.name, store.address, category.name)
                .orderBy(distanceExpr.asc())  // Order by distance
                .limit(30L)
                .fetch();
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

    private BooleanExpression nameContain(String keyword){return store.name.contains(keyword).or(addressContain(keyword));}

    private BooleanExpression addressContain(String keyword) {return store.address.contains(keyword).or(categoryContain(keyword));}

    private BooleanExpression categoryContain(String keyword){ return category.name.contains(keyword).or(hashtagContain(keyword));}

    private BooleanExpression hashtagContain(String keyword){return hashtag.content.contains(keyword);}

}
