package com.hn.api.diary.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFreeBoardPost is a Querydsl query type for FreeBoardPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFreeBoardPost extends EntityPathBase<FreeBoardPost> {

    private static final long serialVersionUID = -291989751L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFreeBoardPost freeBoardPost = new QFreeBoardPost("freeBoardPost");

    public final QDateColumn _super = new QDateColumn(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Integer> depth = createNumber("depth", Integer.class);

    public final ListPath<FreeBoardComment, QFreeBoardComment> freeBoardComments = this.<FreeBoardComment, QFreeBoardComment>createList("freeBoardComments", FreeBoardComment.class, QFreeBoardComment.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDelete = createBoolean("isDelete");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> num = createNumber("num", Integer.class);

    public final NumberPath<Long> origin = createNumber("origin", Long.class);

    public final StringPath title = createString("title");

    public final QUser user;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QFreeBoardPost(String variable) {
        this(FreeBoardPost.class, forVariable(variable), INITS);
    }

    public QFreeBoardPost(Path<? extends FreeBoardPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFreeBoardPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFreeBoardPost(PathMetadata metadata, PathInits inits) {
        this(FreeBoardPost.class, metadata, inits);
    }

    public QFreeBoardPost(Class<? extends FreeBoardPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

