package com.hn.api.diary.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class FreeBoardPost extends DateColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "text")
    private String content;
    private Long viewCount;
    private boolean isDelete;

    @ManyToOne
    private Member member;

    private Long groupId;
    private Integer groupNo;
    private Integer depth;
    private Long parentId;

    @Builder
    public FreeBoardPost(String title, String content, Member member, Long groupId, Long parentId, Integer groupNo, Integer depth) {
        this.title = title;
        this.content = content;
        this.member = member;
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
