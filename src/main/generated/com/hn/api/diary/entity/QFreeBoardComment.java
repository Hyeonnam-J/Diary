package com.hn.api.diary.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFreeBoardComment is a Querydsl query type for FreeBoardComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFreeBoardComment extends EntityPathBase<FreeBoardComment> {

    private static final long serialVersionUID = -16192682L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFreeBoardComment freeBoardComment = new QFreeBoardComment("freeBoardComment");

    public final QDateColumn _super = new QDateColumn(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QFreeBoardPost freeBoardPost;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDelete = createBoolean("isDelete");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Long> origin = createNumber("origin", Long.class);

    public final QUser user;

    public QFreeBoardComment(String variable) {
        this(FreeBoardComment.class, forVariable(variable), INITS);
    }

    public QFreeBoardComment(Path<? extends FreeBoardComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFreeBoardComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFreeBoardComment(PathMetadata metadata, PathInits inits) {
        this(FreeBoardComment.class, metadata, inits);
    }

    public QFreeBoardComment(Class<? extends FreeBoardComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.freeBoardPost = inits.isInitialized("freeBoardPost") ? new QFreeBoardPost(forProperty("freeBoardPost"), inits.get("freeBoardPost")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

