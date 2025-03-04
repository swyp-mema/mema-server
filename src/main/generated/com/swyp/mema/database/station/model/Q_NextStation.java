package com.swyp.mema.database.station.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * Q_NextStation is a Querydsl query type for _NextStation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class Q_NextStation extends EntityPathBase<_NextStation> {

    private static final long serialVersionUID = -565750408L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final Q_NextStation _NextStation = new Q_NextStation("_NextStation");

    public final Q_Station curStation;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> moveTime = createNumber("moveTime", Integer.class);

    public final Q_Station nextStation;

    public final NumberPath<Integer> num1 = createNumber("num1", Integer.class);

    public final NumberPath<Integer> num2 = createNumber("num2", Integer.class);

    public final NumberPath<Integer> num3 = createNumber("num3", Integer.class);

    public Q_NextStation(String variable) {
        this(_NextStation.class, forVariable(variable), INITS);
    }

    public Q_NextStation(Path<? extends _NextStation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public Q_NextStation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public Q_NextStation(PathMetadata metadata, PathInits inits) {
        this(_NextStation.class, metadata, inits);
    }

    public Q_NextStation(Class<? extends _NextStation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.curStation = inits.isInitialized("curStation") ? new Q_Station(forProperty("curStation")) : null;
        this.nextStation = inits.isInitialized("nextStation") ? new Q_Station(forProperty("nextStation")) : null;
    }

}

