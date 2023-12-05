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

    @Builder
    public Comment(Post post, String content) {
        this.post = post;
        this.content = content;
    }
}
