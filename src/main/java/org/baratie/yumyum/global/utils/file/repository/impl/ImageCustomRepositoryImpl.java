package org.baratie.yumyum.global.utils.file.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.global.utils.file.repository.ImageCustomRepository;

import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static org.baratie.yumyum.domain.review.domain.QReview.*;
import static org.baratie.yumyum.global.utils.file.domain.QImage.*;

@RequiredArgsConstructor
public class ImageCustomRepositoryImpl implements ImageCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Map<Long, List<String>> findImageByReviewIdList() {

        return query.select(image.review.id,
                        image.imageUrl)
                .from(image)
                .leftJoin(image.review, review)
                .transform(groupBy(review.id).as(list(image.imageUrl)));
    }

}
