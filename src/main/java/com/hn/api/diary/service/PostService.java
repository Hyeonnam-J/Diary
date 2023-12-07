package com.hn.api.diary.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hn.api.diary.dto.BoardPostsDTO;
import com.hn.api.diary.dto.BoardPostReadDTO;
import com.hn.api.diary.dto.BoardPostReplyDTO;
import com.hn.api.diary.dto.BoardPostUpdateDTO;
import com.hn.api.diary.dto.BoardPostWriteDTO;
import com.hn.api.diary.entity.Post;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.PostRepository;
import com.hn.api.diary.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private class BoardPageSize {
        private static final int BASIC = 10;
    }

    private class BoardSort {
        private static final String BASIC = "basic";
    }

    public BoardPostReadDTO boardPostRead(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(InvalidValue::new);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm:ss");
        String formattedCreatedDate = post.getCreatedDate().format(dateTimeFormatter);

        BoardPostReadDTO dto = new ModelMapper().map(post, BoardPostReadDTO.class);
        dto.setCreatedDate(formattedCreatedDate);

        return dto;
    }

    public void boardPostDelete(String userId, String postDetailId){
        // todo: delete -> put : isDelete true
        // todo: when deleted origin post..
        postRepository.deleteById(Long.parseLong(postDetailId));
    }

    public void boardPostUpdate(BoardPostUpdateDTO boardPostUpdateDTO, String userId, String postDetailId) {
        Post post = postRepository.findById(Long.parseLong(postDetailId))
                .orElseThrow(InvalidValue::new);
        // todo: lastModifiedDate
        post.setTitle(boardPostUpdateDTO.getTitle());
        post.setContent(boardPostUpdateDTO.getContent());

        postRepository.save(post);
    }

    public void boardPostReply(BoardPostReplyDTO boardPostReplyDTO, String userId, String postDetailId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(InvalidValue::new);

        Post originPost = postRepository.findById(Long.parseLong(postDetailId))
                .orElseThrow(InvalidValue::new);

        // 원글 아래 기존 게시물 num + 1
        List<Post> originPosts = postRepository.findByOrigin(originPost.getOrigin());
        originPosts = originPosts.stream()
                .peek(board -> {
                    if (board.getNum() > originPost.getNum())
                        board.setNum(board.getNum() + 1);
                })
                .collect(Collectors.toList());
        postRepository.saveAll(originPosts);

        Post post = Post.builder()
                .title(boardPostReplyDTO.getTitle())
                .content(boardPostReplyDTO.getContent())
                .user(user)
                .origin(originPost.getOrigin())
                .num(originPost.getNum() + 1)
                .depth(originPost.getDepth() + 1)
                .build();
        postRepository.save(post);
    }

    public void boardPostWrite(BoardPostWriteDTO boardPostWriteDTO, String userId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(InvalidValue::new);

        Post post = Post.builder()
                .title(boardPostWriteDTO.getTitle())
                .content(boardPostWriteDTO.getContent())
                .user(user)
                .build();

        postRepository.save(post);

        post.setOrigin(post.getId());
        postRepository.save(post);
    }

    public List<BoardPostsDTO> getBoardPosts(int page, String sort) {
        Pageable pageable;

        switch (sort) {
            case BoardSort.BASIC:
                pageable = PageRequest.of(page, BoardPageSize.BASIC,
                        Sort.by("origin").descending().and(Sort.by("num")));
                break;

            default:
                pageable = PageRequest.of(page, BoardPageSize.BASIC,
                        Sort.by("origin").descending().and(Sort.by("num")));
                break;
        }

        // todo: where isDelete is false
        Iterable<Post> iterablePosts = postRepository.findAll(pageable);
        List<Post> posts = StreamSupport.stream(iterablePosts.spliterator(), false)
                .collect(Collectors.toList());

        // todo: N + 1 issue
         for(Post b : posts){
            System.out.println(">>>>>>>>>>>"+b.getUser().getId());
         }

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

    public int getTotalBoardPostsCount() {
        return (int) postRepository.count();
    }
}
