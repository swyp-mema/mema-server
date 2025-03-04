package com.swyp.mema.database.station.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * Q_Station is a Querydsl query type for _Station
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class Q_Station extends EntityPathBase<_Station> {

    private static final long serialVersionUID = -1032985749L;

    public static final Q_Station _Station = new Q_Station("_Station");

    public final com.swyp.mema.global.base.domain.QBaseEntity _super = new com.swyp.mema.global.base.domain.QBaseEntity(this);

    public final StringPath address = createString("address");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lat = createString("lat");

    public final StringPath lineName = createString("lineName");

    public final StringPath lot = createString("lot");

    public final ListPath<_NextStation, Q_NextStation> nextStations = this.<_NextStation, Q_NextStation>createList("nextStations", _NextStation.class, Q_NextStation.class, PathInits.DIRECT2);

    public final SetPath<_Route, Q_Route> routes = this.<_Route, Q_Route>createSet("routes", _Route.class, Q_Route.class, PathInits.DIRECT2);

    public final StringPath scheduleId = createString("scheduleId");

    public final StringPath stationName = createString("stationName");

    public final ListPath<_TransferStation, Q_TransferStation> transferStations = this.<_TransferStation, Q_TransferStation>createList("transferStations", _TransferStation.class, Q_TransferStation.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public Q_Station(String variable) {
        super(_Station.class, forVariable(variable));
    }

    public Q_Station(Path<? extends _Station> path) {
        super(path.getType(), path.getMetadata());
    }

    public Q_Station(PathMetadata metadata) {
        super(_Station.class, metadata);
    }

}

