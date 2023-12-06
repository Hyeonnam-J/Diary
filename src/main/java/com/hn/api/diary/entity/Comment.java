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
public class Comment extends DateColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Post post;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

    private String content;

    private Long origin;
    private Integer num;
    private Integer depth;

    @Builder
    public Comment(Post post, String content, Long origin, Integer num, Integer depth) {
        this.post = post;
        this.content = content;

        this.origin = origin;   // 테스트 용도?
        this.num = num != null ? num : 0;
        this.depth = depth != null ? depth : 0;
    }
}
