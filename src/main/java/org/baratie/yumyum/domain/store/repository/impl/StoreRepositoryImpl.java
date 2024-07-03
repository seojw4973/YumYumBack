package org.baratie.yumyum.domain.store.repository.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.MainStoreDto;
import org.baratie.yumyum.domain.store.repository.StoreCustomRepository;

import java.util.List;
import java.util.Optional;

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
        JPQLQuery<Long> reviewCount = JPAExpressions
                .select(review.count())
                .from(review)
                .where(review.store.id.eq(store.id));

        JPQLQuery<Double> avgGrade = JPAExpressions
                .select(review.grade.avg())
                .from(review)
                .where(review.store.id.eq(store.id));

        JPQLQuery<Long> favoriteCount = JPAExpressions
                .select(favorite.count())
                .from(favorite)
                .where(favorite.store.id.eq(store.id));

//        List<MainStoreDto> results = query
//                .select(Projections.constructor(MainStoreDto.class,
//                        store.name,
//                        ExpressionUtils.as(avgGrade, "avgGrade"),
//                        ExpressionUtils.as(reviewCount, "reviewCount"),
//                        ExpressionUtils.as(favoriteCount, "favoriteCount")
//                                ))
//                .from(store)
//                .leftJoin(store.review, review)
//                .leftJoin(store.favorite, favorite)
//                .

        return List.of();
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
