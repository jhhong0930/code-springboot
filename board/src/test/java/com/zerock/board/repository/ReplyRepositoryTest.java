package com.zerock.board.repository;

import com.zerock.board.entity.Board;
import com.zerock.board.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    void insertReply() {

        IntStream.rangeClosed(1, 300).forEach(i -> {

            // 1부터 100까지 임의의 번호
            long bno = (long)(Math.random() * 100) + 1;

            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder()
                    .text("Reply = " + i)
                    .board(board)
                    .replyer("guest")
                    .build();
            replyRepository.save(reply);
        });
    }

    /**
     * fetch: JPA에서 연관관계의 데이터를 어떻게 가져올 것인지 정하는것
     * - Eager와 Lazy가 존재
     * Eager loading은 연관관계를 가진 모든 엔티티를 같이 로딩한다
     * 여러 연관관계를 맺고 있거나 복잡할수록 조인으로 인한 성능이 저하될 수 있다
     */
    @Test
    void readReply1() {

        Optional<Reply> result = replyRepository.findById(1L);

        Reply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getBoard());
    }

    @Test
    void testListByBoard() {

        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(97L).build());

        replyList.forEach(reply -> System.out.println(reply));
    }

}