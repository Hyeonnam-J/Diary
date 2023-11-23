package com.hn.api.diary.service;

import com.hn.api.diary.dto.BoardPostsDTO;
import com.hn.api.diary.dto.WriteBoardDTO;
import com.hn.api.diary.entity.Board;
import com.hn.api.diary.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public void post(WriteBoardDTO writeBoardDTO, Long userId){
        System.out.println(writeBoardDTO.getTitle());
        System.out.println(writeBoardDTO.getContent());
        System.out.println(userId);
    }

    public List<BoardPostsDTO> getPosts(Pageable pageable){
        Iterable<Board> iterablePosts = boardRepository.findAll(pageable);
        List<Board> posts = StreamSupport.stream(iterablePosts.spliterator(), false)
                .collect(Collectors.toList());

        // todo: N + 1 issue
//        for(Board b : posts){
//            System.out.println(b.getUser().getId());
//            System.out.println(b.getId());
//        }

        ModelMapper modelMapper = new ModelMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd.");

        return posts.stream()
                .map(p -> {
                    BoardPostsDTO dto = modelMapper.map(p, BoardPostsDTO.class);
                    String strCreatedDate = p.getCreatedDate().format(formatter);
                    dto.setCreatedDate(strCreatedDate);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
