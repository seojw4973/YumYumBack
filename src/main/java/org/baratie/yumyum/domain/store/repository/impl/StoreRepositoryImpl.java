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
        return query.select(Projections.constructor(MainStoreDto.class,
                        store.id,
                        store.name,
                        image.imageUrl,
                        review.grade.avg().as("avgGrade"),
                        review.countDistinct().as("reviewCount"),
                        favorite.countDistinct().as("favoriteCount")
                ))
                .from(store)
                .leftJoin(store.reviewList, review)
                .leftJoin(store.favoriteList, favorite)
                .leftJoin(store.imageList, image)
                .where(store.address.contains(local))
                .groupBy(store.id)
                .orderBy(review.grade.avg().desc(), favorite.count().desc())
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
                .leftJoin(store.reviewList, review)
                .leftJoin(store.favoriteList, favorite)
                .leftJoin(store.imageList, image)
                .where(store.address.contains(local))
                .groupBy(store.id)
                .orderBy(store.views.desc())
                .limit(10L)
                .fetch();
    }

    @Override
    public List<MainStoreDto> findTop10OnFavorite(String local) {
        return query.select(Projections.constructor(MainStoreDto.class,
                        store.id,
                        store.name,
                        image.imageUrl,
                        review.grade.avg().as("avgGrade"),
                        review.countDistinct().as("reviewCount"),
                        favorite.countDistinct().as("favoriteCount")
                ))
                .from(store)
                .leftJoin(store.reviewList, review)
                .leftJoin(store.favoriteList, favorite)
                .leftJoin(store.imageList, image)
                .where(store.address.contains(local))
                .groupBy(store.id)
                .orderBy(favorite.count().desc())
                .limit(10L)
                .fetch();
    }

    @Override
    public List<MainStoreDto> findTop10OnMonth(String local, int year, int month) {

        JPQLQuery<Double> totalReviewGradeAvg = JPAExpressions.select(review.grade.avg())
                .from(review);

        return query.select(Projections.constructor(MainStoreDto.class,
                        store.id,
                        store.name,
                        image.imageUrl,
                        review.grade.avg().as("avgGrade"),
                        review.countDistinct().as("reviewCount"),
                        favorite.countDistinct().as("favoriteCount")
                ))
                .from(store)
                .leftJoin(store.reviewList, review)
                .on(
                        store.address.contains(local),
                        yearEq(year), monthEq(month)
                )
                .leftJoin(store.favoriteList, favorite)
                .leftJoin(store.imageList, image)
                .groupBy(review.store.id)
                .having(
                        review.countDistinct().goe(10),
                        review.grade.avg().goe(totalReviewGradeAvg)
                )
                .orderBy(review.grade.avg().desc(), store.name.desc())
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
                .leftJoin(store.reviewList, review)
                .leftJoin(store.favoriteList, favorite)
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
                .where(favorite.store.id.eq(store.id));

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
                category.name,
                store.longitude,
                store.latitude
                ))
                .from(store)
                .leftJoin(store.reviewList, review)
                .leftJoin(store.favoriteList, favorite)
                .leftJoin(store.imageList, image)
                .leftJoin(store.hashtagList, hashtag)
                .leftJoin(store.categoryList, category)
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
        JPQLQuery<Long> favoriteCount = JPAExpressions
                .select(favorite.count())
                .from(favorite)
                .where(favorite.store.id.eq(store.id));

        JPQLQuery<Long> totalReviewCount = JPAExpressions
                .select(review.count())
                .from(review)
                .where(review.store.id.eq(store.id));

        NumberTemplate<Double> distanceExpr = Expressions.numberTemplate(Double.class,
                "ST_Distance_Sphere(Point({0}, {1}), Point({2}, {3}))",
                store.longitude, store.latitude, lng, lat);

        List<SearchStoreDto> nearbyStoreList = query.select(Projections.constructor(SearchStoreDto.class,
                        store.id,
                        store.name,
                        image.imageUrl,
                        store.address,
                        store.views,
                        Expressions.numberTemplate(Double.class, "round({0}, 2)", review.grade.avg().coalesce(0.0)),
                        totalReviewCount,
                        favoriteCount,
                        favorite.isFavorite,
                        category.name,
                        store.longitude,
                        store.latitude))
                .from(store)
                .leftJoin(store.reviewList, review)
                .leftJoin(store.favoriteList, favorite)
                .leftJoin(store.imageList, image)
                .leftJoin(store.hashtagList, hashtag)
                .leftJoin(store.categoryList, category)
                .where(distanceExpr.loe(1000))
                .groupBy(store.id, store.name, store.address, category.name)
                .orderBy(distanceExpr.asc())
                .limit(30L)
                .fetch();

        for (SearchStoreDto dto : nearbyStoreList) {
            List<String> hashtags = query.select(hashtag.content)
                    .from(hashtag)
                    .where(hashtag.store.id.eq(dto.getStoreId()))
                    .limit(3L)
                    .fetch();
            dto.addHashtagList(hashtags);
        }
        return nearbyStoreList;
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

    private BooleanExpression reviewCount(Long reviewCount) {
        return review.count().goe(reviewCount);
    }

    private BooleanExpression yearEq(int year) {
        return review.createdAt.year().eq(year);
    }

    private BooleanExpression monthEq(int month) {
        return review.createdAt.month().eq(month);
    }


}
