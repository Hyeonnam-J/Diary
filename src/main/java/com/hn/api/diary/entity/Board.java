package com.hn.api.diary.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private Long viewCount;
    private boolean isDelete;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

    private Long origin;
    private Integer num;
    private Integer depth;

    @Builder
    public Board(String title, String content, User user, Long origin, Integer num, Integer depth) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.origin = origin;
        this.num = num;
        this.depth = depth;

        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.lastModifiedDate = now;

        this.viewCount = 0L;
        this.isDelete = false;
    }

    public void setOrigin(Long origin){
        this.origin = origin;
    }
}
