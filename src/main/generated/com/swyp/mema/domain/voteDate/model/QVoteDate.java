package com.swyp.mema.domain.voteDate.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVoteDate is a Querydsl query type for VoteDate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVoteDate extends EntityPathBase<VoteDate> {

    private static final long serialVersionUID = 1994781093L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVoteDate voteDate = new QVoteDate("voteDate");

    public final com.swyp.mema.global.base.domain.QBaseEntity _super = new com.swyp.mema.global.base.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.swyp.mema.domain.meetMember.model.QMeetMember meetMember;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final com.swyp.mema.domain.user.model.QUser user;

    public QVoteDate(String variable) {
        this(VoteDate.class, forVariable(variable), INITS);
    }

    public QVoteDate(Path<? extends VoteDate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVoteDate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVoteDate(PathMetadata metadata, PathInits inits) {
        this(VoteDate.class, metadata, inits);
    }

    public QVoteDate(Class<? extends VoteDate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.meetMember = inits.isInitialized("meetMember") ? new com.swyp.mema.domain.meetMember.model.QMeetMember(forProperty("meetMember"), inits.get("meetMember")) : null;
        this.user = inits.isInitialized("user") ? new com.swyp.mema.domain.user.model.QUser(forProperty("user")) : null;
    }

}

