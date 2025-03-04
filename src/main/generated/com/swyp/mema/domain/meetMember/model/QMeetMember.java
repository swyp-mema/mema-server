package com.swyp.mema.domain.meetMember.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeetMember is a Querydsl query type for MeetMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeetMember extends EntityPathBase<MeetMember> {

    private static final long serialVersionUID = 1028364549L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeetMember meetMember = new QMeetMember("meetMember");

    public final com.swyp.mema.global.base.domain.QBaseEntity _super = new com.swyp.mema.global.base.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final ListPath<com.swyp.mema.domain.voteDate.model.VoteDate, com.swyp.mema.domain.voteDate.model.QVoteDate> dateVotes = this.<com.swyp.mema.domain.voteDate.model.VoteDate, com.swyp.mema.domain.voteDate.model.QVoteDate>createList("dateVotes", com.swyp.mema.domain.voteDate.model.VoteDate.class, com.swyp.mema.domain.voteDate.model.QVoteDate.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.swyp.mema.domain.meet.model.QMeet meet;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final com.swyp.mema.domain.user.model.QUser user;

    public final BooleanPath voteDateYn = createBoolean("voteDateYn");

    public final BooleanPath voteLocationYn = createBoolean("voteLocationYn");

    public QMeetMember(String variable) {
        this(MeetMember.class, forVariable(variable), INITS);
    }

    public QMeetMember(Path<? extends MeetMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeetMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeetMember(PathMetadata metadata, PathInits inits) {
        this(MeetMember.class, metadata, inits);
    }

    public QMeetMember(Class<? extends MeetMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.meet = inits.isInitialized("meet") ? new com.swyp.mema.domain.meet.model.QMeet(forProperty("meet")) : null;
        this.user = inits.isInitialized("user") ? new com.swyp.mema.domain.user.model.QUser(forProperty("user")) : null;
    }

}

