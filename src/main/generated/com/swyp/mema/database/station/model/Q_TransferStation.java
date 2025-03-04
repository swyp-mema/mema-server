package com.swyp.mema.database.station.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * Q_TransferStation is a Querydsl query type for _TransferStation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class Q_TransferStation extends EntityPathBase<_TransferStation> {

    private static final long serialVersionUID = 1288171008L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final Q_TransferStation _TransferStation = new Q_TransferStation("_TransferStation");

    public final Q_Station curStation;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final Q_Station transferStation;

    public final NumberPath<Integer> transferTime = createNumber("transferTime", Integer.class);

    public Q_TransferStation(String variable) {
        this(_TransferStation.class, forVariable(variable), INITS);
    }

    public Q_TransferStation(Path<? extends _TransferStation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public Q_TransferStation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public Q_TransferStation(PathMetadata metadata, PathInits inits) {
        this(_TransferStation.class, metadata, inits);
    }

    public Q_TransferStation(Class<? extends _TransferStation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.curStation = inits.isInitialized("curStation") ? new Q_Station(forProperty("curStation")) : null;
        this.transferStation = inits.isInitialized("transferStation") ? new Q_Station(forProperty("transferStation")) : null;
    }

}

