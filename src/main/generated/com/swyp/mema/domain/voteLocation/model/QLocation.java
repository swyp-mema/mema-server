package com.swyp.mema.domain.voteLocation.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLocation is a Querydsl query type for Location
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLocation extends EntityPathBase<Location> {

    private static final long serialVersionUID = 1127042235L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLocation location = new QLocation("location");

    public final com.swyp.mema.global.base.domain.QBaseEntity _super = new com.swyp.mema.global.base.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lat = createString("lat");

    public final StringPath lot = createString("lot");

    public final com.swyp.mema.domain.meet.model.QMeet meet;

    public final StringPath stationName = createString("stationName");

    public final StringPath stationRoute = createString("stationRoute");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final com.swyp.mema.domain.user.model.QUser user;

    public QLocation(String variable) {
        this(Location.class, forVariable(variable), INITS);
    }

    public QLocation(Path<? extends Location> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLocation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLocation(PathMetadata metadata, PathInits inits) {
        this(Location.class, metadata, inits);
    }

    public QLocation(Class<? extends Location> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.meet = inits.isInitialized("meet") ? new com.swyp.mema.domain.meet.model.QMeet(forProperty("meet")) : null;
        this.user = inits.isInitialized("user") ? new com.swyp.mema.domain.user.model.QUser(forProperty("user")) : null;
    }

}

