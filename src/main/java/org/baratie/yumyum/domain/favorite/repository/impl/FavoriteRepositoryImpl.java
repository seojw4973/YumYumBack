package org.baratie.yumyum.domain.favorite.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.favorite.domain.Favorite;
import org.baratie.yumyum.domain.favorite.repository.FavoriteCustomRepository;

import java.util.Optional;

import static org.baratie.yumyum.domain.favorite.domain.QFavorite.*;

@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Favorite> exist(Long memberId, Long storeId) {

        return Optional.ofNullable(
                query.selectFrom(favorite)
                        .where(memberIdStoreIdEq(memberId, storeId))
                        .fetchOne()
        );
    }

    private BooleanExpression memberIdStoreIdEq(Long memberId, Long storeId) {
        return memberIdEq(memberId).and(storeIdEq(storeId));
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return favorite.member.id.eq(memberId);
    }

    private BooleanExpression storeIdEq(Long storeId) {
        return favorite.store.id.eq(storeId);
    }
}
