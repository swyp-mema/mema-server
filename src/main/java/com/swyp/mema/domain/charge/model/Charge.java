package com.swyp.mema.domain.charge.model;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Charge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meet_id")
    private Meet meet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payee_id")
    private MeetMember payee;

    private String content;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private Integer peopleNum;

    @OneToMany(mappedBy = "charge", cascade = CascadeType.ALL)
    private List<ChargeMember> chargeMembers = new ArrayList<>();

    @Builder
    public Charge(Long id, Meet meet, MeetMember payee, String content, Integer totalPrice, Integer peopleNum) {
        this.id = id;
        this.meet = meet;
        this.payee = payee;
        this.content = content;
        this.totalPrice = totalPrice;
        this.peopleNum = peopleNum;
    }

    public void addChargeMember(ChargeMember chargeMember) {

        chargeMembers.add(chargeMember);
    }
}
