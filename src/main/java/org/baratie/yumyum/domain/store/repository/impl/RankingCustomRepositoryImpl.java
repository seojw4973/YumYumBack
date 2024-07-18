package org.baratie.yumyum.domain.store.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.store.dto.MainStoreDto;
import org.baratie.yumyum.domain.store.repository.RankingCustomRepository;

import java.util.List;

import static org.baratie.yumyum.domain.favorite.domain.QFavorite.favorite;
import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.store;
import static org.baratie.yumyum.global.utils.file.domain.QImage.image;

@RequiredArgsConstructor
public class RankingCustomRepositoryImpl implements RankingCustomRepository {

    private final JPAQueryFactory query;

    /**
     * 리뷰 평점 높은 순 + 즐겨찾기 숫자 많은 순
     * @param local 지역 top10
     */
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

    /**
     * 조회수 기준 top10
     * @param local 지역
     */
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

    /**
     * 즐겨찾기 많은 순 top10
     * @param local 지역
     */
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

    /**
     * 이 달의 맛집 리뷰 갯수가 10개 이상 + 리뷰 평점이 리뷰 전체 평점보다 높은 가게
     * @param local 지역
     * @param year 해당 년도
     * @param month 해당 월
     */
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

    private BooleanExpression yearEq(int year) {
        return review.createdAt.year().eq(year);
    }

    private BooleanExpression monthEq(int month) {
        return review.createdAt.month().eq(month);
    }
}
