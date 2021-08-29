package com.zerock.mreview.repository;

import com.zerock.mreview.entity.Member;
import com.zerock.mreview.entity.Movie;
import com.zerock.mreview.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * EntityGraph - 특정 기능을 수행할 때만 EAGER 로딩을 하도록 지정
     * FETCH - attributePaths에 명시한 속성은 EAGER, 나머지는 LAZY 처리
     * LOAD - 명시된 속성은 EAGER, 나머지는 엔티티 클래스에 명시되거나 기본 방식으로 처리
     */
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    // 회워 삭세를 위한 리뷰 삭제
    @Modifying
    @Query("delete from Review mr where mr.member = :member")
    void deleteByMember(@Param("member") Member member);

}
