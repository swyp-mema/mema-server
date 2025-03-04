package com.swyp.mema.domain.station.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStation is a Querydsl query type for Station
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStation extends EntityPathBase<Station> {

    private static final long serialVersionUID = 953116743L;

    public static final QStation station = new QStation("station");

    public final com.swyp.mema.global.base.domain.QBaseEntity _super = new com.swyp.mema.global.base.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lat = createString("lat");

    public final StringPath lot = createString("lot");

    public final StringPath routeName = createString("routeName");

    public final StringPath stationName = createString("stationName");

    public final NumberPath<Integer> toNext = createNumber("toNext", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final NumberPath<Integer> waitTime = createNumber("waitTime", Integer.class);

    public QStation(String variable) {
        super(Station.class, forVariable(variable));
    }

    public QStation(Path<? extends Station> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStation(PathMetadata metadata) {
        super(Station.class, metadata);
    }

}

