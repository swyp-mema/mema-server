package com.swyp.mema.domain.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class EmailAuth {

    @Id
    private String email;

    int code;

}
