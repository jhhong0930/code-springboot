package com.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.zerock.guestbook.entity.Guestbook;
import com.zerock.guestbook.entity.QGuestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestbookRepositoryTest {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    void insertDummies() {

        IntStream.rangeClosed(1, 300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title = " + i)
                    .content("Content = " + i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });

    }

    /**
     * 수정 시간 테스트
     */
    @Test
    void updateTest() {

        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if (result.isPresent()) {

            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title");
            guestbook.changeContent("Changed Content");

            guestbookRepository.save(guestbook);
        }

    }

    /**
     * 단일 항목 검색 테스트
     */
    @Test
    void testQuery1() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno"));

        // 동적 처리를 위한 Q도메인 클래스 이용
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        // BooleanBuilder는 where문에 들어가는 조건들을 넣어주는 컨테이너 개념
        BooleanBuilder builder = new BooleanBuilder();

        // 원하는 조건을 필드 값과 같이 결합해서 생성
        BooleanExpression expression = qGuestbook.title.contains(keyword);

        // 만들어진 조건을 where문에 and 혹은 or같은 키워드와 결합
        builder.and(expression);

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

    }

    /**
     * 다중 항목 검색 테스트
     */
    @Test
    void testQuery2() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);

        // gno가 0보다 큰
        builder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

    }

}