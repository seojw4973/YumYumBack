package org.baratie.yumyum.domain.reply.repository;

import org.baratie.yumyum.domain.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyCustomRepository {

    @Query("DELETE Reply r WHERE r.review.id = :reviewId")
    void deleteByReviewId(Long reviewId);

}
