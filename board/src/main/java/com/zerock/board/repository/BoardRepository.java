package com.zerock.board.repository;

import com.zerock.board.entity.Board;
import com.zerock.board.repository.search.SearchBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

    // 한 행(Object) 내에 Object[]로 나온다
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    /**
     * select board.bno, board.title, board.writer_email, rno, text
     * from board left outer join reply on reply.board_bno = board.bno
     * where board.bno = 100; 와 같은 결과
     */
    @Query("select b, r from Board b left join Reply r on r.board = b where b.bno =:bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    // 목록 화면에 필요한 데이터
    @Query(value = "select b, w, count(r) " +
            " from Board b " +
            " left join b.writer w " +
            " left join Reply r on r.board = b " +
            " group by b",
            countQuery = "select count(b) from Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    // 조회 화면에 필요한 JPQL
    @Query("select b, w, count(r) " +
            " from Board b left join b.writer w " +
            " left outer join Reply r on r.board = b " +
            " where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

}
