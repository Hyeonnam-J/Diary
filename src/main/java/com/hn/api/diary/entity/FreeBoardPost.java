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
public class FreeBoardPost extends DateColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;
    private Long viewCount;
    private boolean isDelete;

    @ManyToOne
    private User user;

    private Long groupId;
    private Integer groupNo;
    private Integer depth;
    private Long parentId;

    @Builder
    public FreeBoardPost(String title, String content, User user, Long groupId, Long parentId, Integer groupNo, Integer depth) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.groupId = groupId;
        this.groupNo = groupNo == null ? 0 : groupNo;
        this.depth = depth == null ? 0 : depth;
        this.parentId = parentId == null ? 0 : parentId;
        this.viewCount = 0L;
        this.isDelete = false;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
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

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
