package org.baratie.yumyum.domain.store.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.store.dto.SearchStoreDto;
import org.baratie.yumyum.domain.store.repository.SearchCustomRepository;

import java.util.List;

import static org.baratie.yumyum.domain.category.domain.QCategory.category;
import static org.baratie.yumyum.domain.favorite.domain.QFavorite.favorite;
import static org.baratie.yumyum.domain.hashtag.domain.QHashtag.hashtag;
import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.store;
import static org.baratie.yumyum.global.utils.file.domain.QImage.image;

@RequiredArgsConstructor
public class SearchCustomRepositoryImpl implements SearchCustomRepository {

    private final JPAQueryFactory query;

    /**
     * 키워드 검색 결과
     * @param memberId 즐겨찾기 상태도 같이 보내주기 위한 멤버 Id
     * @param keyword 검색 키워드
     * @return 내 주변 맛집 30개
     */
    @Override
    public List<SearchStoreDto> findSearchStore(Long memberId, String keyword) {
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
                .where(searchCondition(keyword))
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

    /**
     * 내 주변 반경 1km 맛집
     * @param lng 위도
     * @param lat 경토
     * @return 내 주변 맛집 30개 조회
     */
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

    private BooleanExpression searchCondition(String keyword) {
        return nameContain(keyword)
                .or(addressContain(keyword)
                .or(categoryContain(keyword)
                .or(hashtagContain(keyword))
                ));
    }

    private BooleanExpression nameContain(String keyword) {
        return store.name.contains(keyword);
    }

    private BooleanExpression addressContain(String keyword) {
        return store.address.contains(keyword);
    }


    private BooleanExpression categoryContain(String keyword) {
        return category.name.contains(keyword);
    }

    private BooleanExpression hashtagContain(String keyword) {
        return hashtag.content.contains(keyword);
    }

}