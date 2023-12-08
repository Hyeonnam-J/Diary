package com.hn.api.diary.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Post extends DateColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private Long viewCount;
    private boolean isDelete;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

    private Long origin;
    private Integer num;
    private Integer depth;

    @Builder
    public Post(String title, String content, User user, Long origin, Integer num, Integer depth) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.origin = origin;
        this.num = num != null ? num : 0;
        this.depth = depth != null ? depth : 0;

        this.viewCount = 0L;
        this.isDelete = false;
    }

    public void setOrigin(Long origin){
        this.origin = origin;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    List<Comment> comments = new ArrayList<>();
}
