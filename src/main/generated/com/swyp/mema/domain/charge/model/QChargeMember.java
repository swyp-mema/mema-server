package com.swyp.mema.domain.charge.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChargeMember is a Querydsl query type for ChargeMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChargeMember extends EntityPathBase<ChargeMember> {

    private static final long serialVersionUID = -1799430689L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChargeMember chargeMember = new QChargeMember("chargeMember");

    public final com.swyp.mema.global.base.domain.QBaseEntity _super = new com.swyp.mema.global.base.domain.QBaseEntity(this);

    public final QCharge charge;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.swyp.mema.domain.meetMember.model.QMeetMember payer;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QChargeMember(String variable) {
        this(ChargeMember.class, forVariable(variable), INITS);
    }

    public QChargeMember(Path<? extends ChargeMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChargeMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChargeMember(PathMetadata metadata, PathInits inits) {
        this(ChargeMember.class, metadata, inits);
    }

    public QChargeMember(Class<? extends ChargeMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.charge = inits.isInitialized("charge") ? new QCharge(forProperty("charge"), inits.get("charge")) : null;
        this.payer = inits.isInitialized("payer") ? new com.swyp.mema.domain.meetMember.model.QMeetMember(forProperty("payer"), inits.get("payer")) : null;
    }

}

