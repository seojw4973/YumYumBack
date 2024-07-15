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

import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.*;
import static org.baratie.yumyum.domain.favorite.domain.QFavorite.favorite;
import static org.baratie.yumyum.domain.category.domain.QCategory.category;
import static org.baratie.yumyum.domain.hashtag.domain.QHashtag.*;
import static org.baratie.yumyum.global.utils.file.domain.QImage.image;

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

    /**
     * 즐겨찾기한 맛집
     */
    @Override
    public Slice<MyFavoriteStoreDto> findFavoriteStore(Long memberId, Pageable pageable) {

        JPQLQuery<Double> avgGrade = JPAExpressions
                .select(review.grade.avg())
                .from(review)
                .where(review.store.id.eq(store.id));

        JPQLQuery<Long> favoriteCount = JPAExpressions
                .select(favorite.count())
                .from(favorite)
                .where(favorite.store.id.eq(store.id));

        JPQLQuery<Long> totalReviewCount = JPAExpressions
                .select(review.count())
                .from(review)
                .where(review.store.id.eq(store.id));

        List<MyFavoriteStoreDto> results = query.select(
                Projections.constructor(MyFavoriteStoreDto.class,
                        store.id,
                        store.name,
                        image.imageUrl,
                        store.address,
                        store.views,
                        avgGrade,
                        totalReviewCount,
                        favoriteCount,
                        favorite.isFavorite,
                        category.name
                ))
                .from(store)
                .leftJoin(store.reviewList, review)
                .leftJoin(store.favoriteList, favorite)
                .leftJoin(store.imageList, image)
                .leftJoin(store.categoryList, category)
                .where(memberIdEq(memberId), favoriteEq(true))
                .groupBy(store.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        for (MyFavoriteStoreDto dto : results) {
            List<String> hashtags = query.select(hashtag.content)
                    .from(hashtag)
                    .where(hashtag.store.id.eq(dto.getStoreId()))
                    .limit(3L)
                    .fetch();
            dto.addHashtagList(hashtags);
        }

        boolean hasNext = results.size() > pageable.getPageSize();

        if (hasNext) {
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private BooleanExpression storeIdEq(Long storeId) {
        return store.id.eq(storeId);
    }

    private BooleanExpression memberIdEq(Long memberId) { return favorite.member.id.eq(memberId);}

    private BooleanExpression favoriteEq(boolean status) { return favorite.isFavorite.eq(status);}

}
