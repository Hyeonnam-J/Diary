package com.hn.api.diary.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLocalDateTimeEntity is a Querydsl query type for LocalDateTimeEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QLocalDateTimeEntity extends EntityPathBase<DateColumn> {

    private static final long serialVersionUID = 1595989976L;

    public static final QLocalDateTimeEntity localDateTimeEntity = new QLocalDateTimeEntity("localDateTimeEntity");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = createDateTime("lastModifiedDate", java.time.LocalDateTime.class);

    public QLocalDateTimeEntity(String variable) {
        super(DateColumn.class, forVariable(variable));
    }

    public QLocalDateTimeEntity(Path<? extends DateColumn> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLocalDateTimeEntity(PathMetadata metadata) {
        super(DateColumn.class, metadata);
    }

}

