package org.baratie.yumyum.domain.hashtag.repository.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.hashtag.repository.HashtagCustomRepository;

import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static org.baratie.yumyum.domain.hashtag.domain.QHashtag.*;
import static org.baratie.yumyum.domain.store.domain.QStore.*;

@RequiredArgsConstructor
public class HashtagCustomRepositoryImpl implements HashtagCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Map<Long, List<String>> findHashtagByStoreId() {
        return query.select(hashtag.id, hashtag.content)
                .from(hashtag)
                .leftJoin(hashtag.store, store)
                .transform(groupBy(store.id).as(list(hashtag.content)));
    }
}
