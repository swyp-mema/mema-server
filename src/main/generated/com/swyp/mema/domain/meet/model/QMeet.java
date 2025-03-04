package com.swyp.mema.domain.meet.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeet is a Querydsl query type for Meet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeet extends EntityPathBase<Meet> {

    private static final long serialVersionUID = -1912129083L;

    public static final QMeet meet = new QMeet("meet");

    public final com.swyp.mema.global.base.domain.QBaseEntity _super = new com.swyp.mema.global.base.domain.QBaseEntity(this);

    public final NumberPath<Integer> code = createNumber("code", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final DateTimePath<java.time.LocalDateTime> expiredVoteDate = createDateTime("expiredVoteDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lat = createString("lat");

    public final StringPath line = createString("line");

    public final StringPath lot = createString("lot");

    public final DatePath<java.time.LocalDate> meetDate = createDate("meetDate", java.time.LocalDate.class);

    public final StringPath meetLocation = createString("meetLocation");

    public final ListPath<com.swyp.mema.domain.meetMember.model.MeetMember, com.swyp.mema.domain.meetMember.model.QMeetMember> members = this.<com.swyp.mema.domain.meetMember.model.MeetMember, com.swyp.mema.domain.meetMember.model.QMeetMember>createList("members", com.swyp.mema.domain.meetMember.model.MeetMember.class, com.swyp.mema.domain.meetMember.model.QMeetMember.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final EnumPath<com.swyp.mema.domain.meet.model.vo.State> state = createEnum("state", com.swyp.mema.domain.meet.model.vo.State.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QMeet(String variable) {
        super(Meet.class, forVariable(variable));
    }

    public QMeet(Path<? extends Meet> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMeet(PathMetadata metadata) {
        super(Meet.class, metadata);
    }

}

