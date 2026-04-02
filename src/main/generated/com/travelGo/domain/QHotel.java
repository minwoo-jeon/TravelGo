package com.travelGo.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHotel is a Querydsl query type for Hotel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHotel extends EntityPathBase<Hotel> {

    private static final long serialVersionUID = -1467962359L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHotel hotel = new QHotel("hotel");

    public final com.travelGo.global.common.QBaseEntity _super = new com.travelGo.global.common.QBaseEntity(this);

    public final com.travelGo.global.common.QAddress address;

    public final StringPath businessNumber = createString("businessNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QMember owner;

    public final ListPath<Room, QRoom> rooms = this.<Room, QRoom>createList("rooms", Room.class, QRoom.class, PathInits.DIRECT2);

    public final NumberPath<Integer> starRating = createNumber("starRating", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public QHotel(String variable) {
        this(Hotel.class, forVariable(variable), INITS);
    }

    public QHotel(Path<? extends Hotel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHotel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHotel(PathMetadata metadata, PathInits inits) {
        this(Hotel.class, metadata, inits);
    }

    public QHotel(Class<? extends Hotel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new com.travelGo.global.common.QAddress(forProperty("address")) : null;
        this.owner = inits.isInitialized("owner") ? new QMember(forProperty("owner")) : null;
    }

}

