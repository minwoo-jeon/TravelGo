package com.travelGo.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoom is a Querydsl query type for Room
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoom extends EntityPathBase<Room> {

    private static final long serialVersionUID = -1155434522L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoom room = new QRoom("room");

    public final com.travelGo.global.common.QBaseEntity _super = new com.travelGo.global.common.QBaseEntity(this);

    public final NumberPath<Integer> baseCapacity = createNumber("baseCapacity", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final QHotel hotel;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<RoomInventory, QRoomInventory> inventories = this.<RoomInventory, QRoomInventory>createList("inventories", RoomInventory.class, QRoomInventory.class, PathInits.DIRECT2);

    public final NumberPath<Integer> maxCapacity = createNumber("maxCapacity", Integer.class);

    public final StringPath name = createString("name");

    public final EnumPath<RoomStatus> status = createEnum("status", RoomStatus.class);

    public final NumberPath<Integer> totalCount = createNumber("totalCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public final NumberPath<Integer> weekdayPrice = createNumber("weekdayPrice", Integer.class);

    public final NumberPath<Integer> weekendPrice = createNumber("weekendPrice", Integer.class);

    public QRoom(String variable) {
        this(Room.class, forVariable(variable), INITS);
    }

    public QRoom(Path<? extends Room> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoom(PathMetadata metadata, PathInits inits) {
        this(Room.class, metadata, inits);
    }

    public QRoom(Class<? extends Room> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hotel = inits.isInitialized("hotel") ? new QHotel(forProperty("hotel"), inits.get("hotel")) : null;
    }

}

