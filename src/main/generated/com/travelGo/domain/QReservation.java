package com.travelGo.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservation is a Querydsl query type for Reservation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservation extends EntityPathBase<Reservation> {

    private static final long serialVersionUID = -1836950943L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservation reservation = new QReservation("reservation");

    public final com.travelGo.global.common.QBaseEntity _super = new com.travelGo.global.common.QBaseEntity(this);

    public final DatePath<java.time.LocalDate> checkInDate = createDate("checkInDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> checkOutDate = createDate("checkOutDate", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final QHotel hotel;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final NumberPath<Integer> reservationCount = createNumber("reservationCount", Integer.class);

    public final QRoom room;

    public final EnumPath<ReservationStatus> status = createEnum("status", ReservationStatus.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public QReservation(String variable) {
        this(Reservation.class, forVariable(variable), INITS);
    }

    public QReservation(Path<? extends Reservation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservation(PathMetadata metadata, PathInits inits) {
        this(Reservation.class, metadata, inits);
    }

    public QReservation(Class<? extends Reservation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hotel = inits.isInitialized("hotel") ? new QHotel(forProperty("hotel"), inits.get("hotel")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.room = inits.isInitialized("room") ? new QRoom(forProperty("room"), inits.get("room")) : null;
    }

}

