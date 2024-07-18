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
import java.util.Map;

import static org.baratie.yumyum.domain.category.domain.QCategory.category;
import static org.baratie.yumyum.domain.favorite.domain.QFavorite.favorite;
import static org.baratie.yumyum.domain.hashtag.domain.QHashtag.hashtag;
import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.store;
import static org.baratie.yumyum.domain.menu.domain.QMenu.menu;

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
    public List<SearchStoreDto> findSearchStore(Long memberId, Map<Long, String> imageMap,Map<Long, List<String>> hashtagMap, String keyword) {
        JPQLQuery<Long> favoriteCount = JPAExpressions
                .select(favorite.count())
                .from(favorite)
                .where(favorite.store.id.eq(store.id));

        JPQLQuery<Long> totalReviewCount = JPAExpressions
                .select(review.count())
                .from(review)
                .where(review.store.id.eq(store.id));

        JPQLQuery<Boolean> favoriteStatus = JPAExpressions.select(favorite.isFavorite)
                .from(favorite)
                .where(favorite.store.id.eq(store.id), favorite.member.id.eq(memberId));

        List<SearchStoreDto> searchStoreList = query.select(Projections.constructor(SearchStoreDto.class,
                        store.id,
                        store.name,
                        store.address,
                        store.views,
                        Expressions.numberTemplate(Double.class, "round({0}, 2)", review.grade.avg().coalesce(0.0)),
                        totalReviewCount,
                        favoriteCount,
                        favoriteStatus,
                        category.name,
                        store.longitude,
                        store.latitude
                ))
                .from(store)
                .leftJoin(store.reviewList, review)
                .leftJoin(store.favoriteList, favorite)
                .leftJoin(store.categoryList, category)
                .leftJoin(store.menuList, menu)
                .leftJoin(store.hashtagList, hashtag)
                .where(searchCondition(keyword))
                .groupBy(store.id, store.name, store.address, category.name)
                .limit(30L)
                .fetch();

        return getSearchStoreDtos(searchStoreList, imageMap, hashtagMap);
    }

    /**
     * 내 주변 반경 1km 맛집
     * @param lng 위도
     * @param lat 경토
     * @return 내 주변 맛집 30개 조회
     */
    @Override
    public List<SearchStoreDto> findNearByStore(Long memberId, Double lng, Double lat, Map<Long, String> imageMap, Map<Long, List<String>> hashtagMap) {
        JPQLQuery<Long> favoriteCount = JPAExpressions
                .select(favorite.count())
                .from(favorite)
                .where(favorite.store.id.eq(store.id));

        JPQLQuery<Long> totalReviewCount = JPAExpressions
                .select(review.count())
                .from(review)
                .where(review.store.id.eq(store.id));

        JPQLQuery<Boolean> favoriteStatus = JPAExpressions.select(favorite.isFavorite)
                .from(favorite)
                .where(favorite.store.id.eq(store.id), favorite.member.id.eq(memberId));

        NumberTemplate<Double> distanceExpr = Expressions.numberTemplate(Double.class,
                "ST_Distance_Sphere(Point({0}, {1}), Point({2}, {3}))",
                store.longitude, store.latitude, lng, lat);

        List<SearchStoreDto> nearbyStoreList = query.select(Projections.constructor(SearchStoreDto.class,
                        store.id,
                        store.name,
                        store.address,
                        store.views,
                        Expressions.numberTemplate(Double.class, "round({0}, 2)", review.grade.avg().coalesce(0.0)),
                        totalReviewCount,
                        favoriteCount,
                        favoriteStatus,
                        category.name,
                        store.longitude,
                        store.latitude))
                .from(store)
                .leftJoin(store.reviewList, review)
                .leftJoin(store.favoriteList, favorite)
                .leftJoin(store.categoryList, category)
                .where(distanceExpr.loe(1000))
                .groupBy(store.id, store.name, store.address, category.name)
                .orderBy(distanceExpr.asc())
                .limit(30L)
                .fetch();

        return getSearchStoreDtos(nearbyStoreList, imageMap, hashtagMap);
    }


    private List<SearchStoreDto> getSearchStoreDtos(List<SearchStoreDto> dtos, Map<Long, String> imageMap, Map<Long, List<String>> hashtagMap) {
        dtos.forEach(dto -> {
            String image = imageMap.get(dto.getStoreId());
            if(image != null) {
                dto.addImage(image);
            }
            List<String> hashtags = hashtagMap.get(dto.getStoreId());
            if(hashtags != null) {
                dto.addHashtagList(hashtags);
            }
        });
        return dtos;
    }

    private BooleanExpression searchCondition(String keyword) {
        return nameContain(keyword)
                .or(addressContain(keyword)
                .or(categoryContain(keyword)
                .or(menuContain(keyword)
                .or(hashtagContain(keyword))
                )));
    }

    private BooleanExpression nameContain(String keyword) {
        return store.name.contains(keyword);
    }

    private BooleanExpression addressContain(String keyword) {
        return store.address.contains(keyword);
    }

    private BooleanExpression menuContain(String keyword) { return menu.name.contains(keyword);}

    private BooleanExpression categoryContain(String keyword) {
        return category.name.contains(keyword);
    }

    private BooleanExpression hashtagContain(String keyword) {
        return hashtag.content.contains(keyword);
    }

}
