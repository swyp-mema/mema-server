package com.swyp.mema.domain.charge.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCharge is a Querydsl query type for Charge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCharge extends EntityPathBase<Charge> {

    private static final long serialVersionUID = 1315497701L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCharge charge = new QCharge("charge");

    public final com.swyp.mema.global.base.domain.QBaseEntity _super = new com.swyp.mema.global.base.domain.QBaseEntity(this);

    public final ListPath<ChargeMember, QChargeMember> chargeMembers = this.<ChargeMember, QChargeMember>createList("chargeMembers", ChargeMember.class, QChargeMember.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.swyp.mema.domain.meet.model.QMeet meet;

    public final com.swyp.mema.domain.meetMember.model.QMeetMember payee;

    public final NumberPath<Integer> peopleNum = createNumber("peopleNum", Integer.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QCharge(String variable) {
        this(Charge.class, forVariable(variable), INITS);
    }

    public QCharge(Path<? extends Charge> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCharge(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCharge(PathMetadata metadata, PathInits inits) {
        this(Charge.class, metadata, inits);
    }

    public QCharge(Class<? extends Charge> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.meet = inits.isInitialized("meet") ? new com.swyp.mema.domain.meet.model.QMeet(forProperty("meet")) : null;
        this.payee = inits.isInitialized("payee") ? new com.swyp.mema.domain.meetMember.model.QMeetMember(forProperty("payee"), inits.get("payee")) : null;
    }

}

