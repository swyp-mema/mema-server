package com.swyp.mema.domain.charge.converter;

import com.swyp.mema.domain.charge.dto.request.ChargeReq;
import com.swyp.mema.domain.charge.dto.response.ChargeRes;
import com.swyp.mema.domain.charge.dto.response.PayerInfo;
import com.swyp.mema.domain.charge.model.Charge;
import com.swyp.mema.domain.charge.model.ChargeMember;
import com.swyp.mema.domain.meet.repository.MeetRepository;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChargeConverter {

    private final MeetRepository meetRepository;
    private final MeetMemberRepository meetMemberRepository;
    private final UserRepository userRepository;

    public Charge generateCharge(Long meetId, ChargeReq req, Long payeeId) {

        // charge 생성
        Charge charge = Charge.builder()
                .meet(meetRepository.getReferenceById(meetId))
                .payee(meetMemberRepository.getReferenceById(payeeId))
                .content(req.getContent())
                .totalPrice(req.getTotalPrice())
                .peopleNum(req.getPeopleNumber())
                .build();

        int price = req.getTotalPrice()/req.getPeopleNumber();

        //payer id를 뽑아 charge_member 생성
        List<Long> payerIds = req.getMemberIds();
        for(Long payerId : payerIds) {

            charge.addChargeMember(ChargeMember.builder()
                    .charge(charge)
                    .payer(meetMemberRepository.getReferenceById(payerId))
                    .price(price)
                    .build());
        }

        return charge;
    }

    public ChargeRes toChargeRes(Charge charge) {
        return ChargeRes.builder()
                .chargeId(charge.getId())
                .content(charge.getContent())
                .totalPrice(charge.getTotalPrice())
                .peopleNumber(charge.getPeopleNum())
                .payeeNickname(charge.getPayee().getUser().getNickname())
                .members(charge.getChargeMembers().stream()
                        .map(this::toPayerInfo)
                        .collect(Collectors.toList()))
                .build();
    }

    public List<ChargeRes> toChargeReses(List<Charge> charges) {

        System.out.println("ChargeConverter - toChargeReses");

        List<ChargeRes> res = new ArrayList<>();
        for(Charge charge : charges) {
            res.add(toChargeRes(charge));
        }
        return res;
    }

    private PayerInfo toPayerInfo(ChargeMember chargeMember) {

        boolean isMe = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()) == chargeMember.getPayer().getUser().getUserId();

        return PayerInfo.builder()
                .memberId(chargeMember.getPayer().getId())
                .nickname(chargeMember.getPayer().getUser().getNickname())
                .isMe(isMe)
                .build();
    }
}
