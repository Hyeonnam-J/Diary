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
    public Board(String title, String content, User user, LocalDateTime createdDate, Long origin, Integer num, Integer depth) {
        this.title = title;
        this.content = content;

        this.user = user;   // ?
        this.createdDate = createdDate;
        this.origin = origin;   // 테스트 용도
        this.num = num;
        this.depth = depth;

        this.viewCount = 0L;
        this.isDelete = false;
    }
}
