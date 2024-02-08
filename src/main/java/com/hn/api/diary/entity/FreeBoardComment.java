package com.hn.api.diary.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@Getter
public class FreeBoardComment extends DateColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FreeBoardPost freeBoardPost;

    @ManyToOne
    private Member member;

    @Lob
    private String content;
    private boolean isDelete;

    private Long groupId;
    private boolean isParent;

    @Builder
    public FreeBoardComment(FreeBoardPost freeBoardPost, Member member, String content, Long groupId, boolean isParent) {
        this.freeBoardPost = freeBoardPost;
        this.member = member;
        this.content = content;

        this.isDelete = false;
        this.groupId = groupId;
        this.isParent = isParent;   // null이면 false
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
