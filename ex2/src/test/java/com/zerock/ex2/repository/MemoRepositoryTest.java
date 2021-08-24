package com.zerock.ex2.repository;

import com.zerock.ex2.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    /**
     * 페이징 처리 테스트
     * of(int page, int size): 0부터 시작하는 페이지 번호와 개수, 정렬이 지정되지 않음
     * of(int page, int size, Sort.Direction direction, String...props):
     * - 0부터 시작하는 페이지 번호와 개수, 정렬의 방향과 정렬 기준 필드들
     * of(int page, int size, Sort sort): 페이지 번호와 개수, 정렬 관련 정보
     */
    @Test
    void testPageDefault() {

        // 1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("===============================");

        // 페이지 총 개수
        System.out.println("result.getTotalPages() = " + result.getTotalPages());
        // 전체 개수
        System.out.println("result.getTotalElements() = " + result.getTotalElements());
        // 현재 페이지 번호 0부터 시작
        System.out.println("result.getNumber( = " + result.getNumber());
        // 페이지당 데이터 개수
        System.out.println("result.getSize() = " + result.getSize());
        // 다음 페이지 존재 여부
        System.out.println("result.hasNext() = " + result.hasNext());
        // 시작 페이지(0) 여부
        System.out.println("result.isFirst() = " + result.isFirst());

        System.out.println("===============================");

        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }

    }

    @Test
    void testSort() {

        Sort sort1 = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(2, 10, sort1);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });

    }

}