package com.zerock.ex2.repository;

import com.zerock.ex2.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemoRepositoryTest {

    @Autowired MemoRepository memoRepository;

    @Test
    void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    void testInsertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample = " + i).build();
            memoRepository.save(memo);
        });
    }

    /**
     * 조회 작업 테스트
     * findById -> Optional 타입으로 반환
     */
    @Test
    void testSelect() {
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("========================");
        if (result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Test
    @Transactional
    void testSelect2() {
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);
        System.out.println("========================");
        System.out.println(memo);
    }

    /**
     * 수정 작업 테스트
     * 등록 작업과 동일하게 save()를 이용하여 처리
     * 해당 @Id를 가진 엔티티 객체가 있다면 update, 없다면 insert 실행
     */
    @Test
    void updateTest() {
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();

        System.out.println(memoRepository.save(memo));
    }

    /**
     * 삭제 작업 테스트
     * 삭제하려는 번호의 엔티티 객체가 있는지 확인 후 삭제
     * 해당 데이터가 존재하지 않으면 EmptyResultDataAccessException 예외 발생
     */
    @Test
    void testDelete() {
        Long mno = 100L;

        memoRepository.deleteById(mno);
    }

}