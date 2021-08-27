package com.zerock.board.service;

import com.zerock.board.dto.BoardDTO;
import com.zerock.board.dto.PageRequestDTO;
import com.zerock.board.dto.PageResultDTO;
import com.zerock.board.entity.Board;
import com.zerock.board.entity.Member;
import com.zerock.board.repository.BoardRepository;
import com.zerock.board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService{

    private final BoardRepository repository;
    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {

        log.info("dto={}", dto);

        Board board = dtoToEntity(dto);

        repository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info("pageRequestDTO={}", pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en -> entityToDTO(
                (Board)en[0],
                (Member)en[1],
                (Long)en[2])
        );

//        Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending()
                ));

        return new PageResultDTO<>(result, fn);

    }

    @Override
    public BoardDTO get(Long bno) {

        Object result = repository.getBoardByBno(bno);

        Object[] arr = (Object[])result;

        return entityToDTO((Board) arr[0], (Member) arr[1], (Long) arr[2]);

    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) { // 삭제 기능 구현, 트랜잭션 추가

        // 댓글 먼저 삭제
        replyRepository.deleteByBno(bno);

        repository.deleteById(bno);
    }

    @Override
    public void modify(BoardDTO boardDTO) {

        Board board = repository.getOne(boardDTO.getBno());

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(board.getContent());

        repository.save(board);

    }

}
