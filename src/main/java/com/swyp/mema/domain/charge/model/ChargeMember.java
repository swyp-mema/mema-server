package com.swyp.mema.domain.charge.model;

import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChargeMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_id")
    private Charge charge;

    @ManyToOne(fetch = FetchType.LAZY) // 돈 내는사람
    @JoinColumn(name = "payer_id")
    private MeetMember payer;

    @Column(nullable = false)
    private Integer price;

    @Builder
    public ChargeMember (Charge charge, MeetMember payer, Integer price) {

        this.charge = charge;
        this.payer = payer;
        this.price = price;
    }
}
