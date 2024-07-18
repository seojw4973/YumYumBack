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

    /**
     * 특정 가게 즐겨찾기 했는지 확인
     * @param memberId 로그인한 멤버 id
     * @param storeId 즐겨찾기 누를 가게 id
     * @return BooleanExpression
     */
    private BooleanExpression memberIdStoreIdEq(Long memberId, Long storeId) {
        return memberIdEq(memberId).and(storeIdEq(storeId));
    }

    /**
     * 로그인한 멤버가 즐겨찾기 눌렀는지 확인
     * @param memberId 로그인한 멤버 id
     * @return BooleanExpression
     */
    private BooleanExpression memberIdEq(Long memberId) {
        return favorite.member.id.eq(memberId);
    }

    /**
     * 즐겨찾기 누른 가게가 있는지 확인
     * @param storeId 즐겨찾기 누른 가게 id
     * @return
     */
    private BooleanExpression storeIdEq(Long storeId) {
        return favorite.store.id.eq(storeId);
    }
}
