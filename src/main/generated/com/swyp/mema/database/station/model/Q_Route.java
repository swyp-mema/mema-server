package com.swyp.mema.database.station.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * Q_Route is a Querydsl query type for _Route
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class Q_Route extends EntityPathBase<_Route> {

    private static final long serialVersionUID = 2058204736L;

    public static final Q_Route _Route = new Q_Route("_Route");

    public final StringPath line = createString("line");

    public final NumberPath<Integer> num1 = createNumber("num1", Integer.class);

    public final NumberPath<Integer> num2 = createNumber("num2", Integer.class);

    public final NumberPath<Integer> num3 = createNumber("num3", Integer.class);

    public final StringPath route = createString("route");

    public final NumberPath<Integer> time1 = createNumber("time1", Integer.class);

    public final NumberPath<Integer> time2 = createNumber("time2", Integer.class);

    public final NumberPath<Integer> time3 = createNumber("time3", Integer.class);

    public Q_Route(String variable) {
        super(_Route.class, forVariable(variable));
    }

    public Q_Route(Path<? extends _Route> path) {
        super(path.getType(), path.getMetadata());
    }

    public Q_Route(PathMetadata metadata) {
        super(_Route.class, metadata);
    }

}

