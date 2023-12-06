package com.hn.api.diary.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDateColumn is a Querydsl query type for DateColumn
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QDateColumn extends EntityPathBase<DateColumn> {

    private static final long serialVersionUID = -292354155L;

    public static final QDateColumn dateColumn = new QDateColumn("dateColumn");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = createDateTime("lastModifiedDate", java.time.LocalDateTime.class);

    public QDateColumn(String variable) {
        super(DateColumn.class, forVariable(variable));
    }

    public QDateColumn(Path<? extends DateColumn> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDateColumn(PathMetadata metadata) {
        super(DateColumn.class, metadata);
    }

}

