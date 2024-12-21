package com.swyp.mema.domain.charge.service;

import com.swyp.mema.domain.charge.converter.ChargeConverter;
import com.swyp.mema.domain.charge.dto.request.ChargeReq;
import com.swyp.mema.domain.charge.dto.response.ChargeRes;
import com.swyp.mema.domain.charge.exception.ChargeNotFoundException;
import com.swyp.mema.domain.charge.model.Charge;
import com.swyp.mema.domain.charge.model.ChargeMember;
import com.swyp.mema.domain.charge.repository.ChargeRepository;
import com.swyp.mema.domain.meet.exception.MeetNotFoundException;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.domain.meet.repository.MeetRepository;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.user.dto.CustomUserDetails;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargeService {

    private final ChargeRepository chargeRepository;
    private final ChargeConverter chargeConverter;
    private final MeetRepository meetRepository;
    private final MeetMemberRepository meetMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createCharge(Long meetId, ChargeReq req, CustomUserDetails userDetails) {

        Meet meet = meetRepository.findById(meetId)
            .orElseThrow(MeetNotFoundException::new);

        Charge charge = chargeConverter.generateCharge(meetId, req, userDetails.getUserId());
        chargeRepository.save(charge);
        meet.changeState(State.SETTLING);
    }

    @Transactional(readOnly = true)
    public List<ChargeRes> getCharges(Long meetId){

        if(!meetRepository.existsById(meetId)){

            throw new MeetNotFoundException();
        }

        /*
            해당 미팅에 대한 권한이 있는지 체크(해당 미팅에 가입했는지)
         */
        List<Charge> charges = chargeRepository.findByMeetId(meetId);
        return chargeConverter.toChargeReses(charges);
    }

    @Transactional(readOnly = true)
    public List<ChargeRes> getCharge(Long meetId, Long chargeId){

        if(!meetRepository.existsById(meetId)){

            throw new MeetNotFoundException();
        }

        /*
            해당 미팅에 대한 권한이 있는지 체크(해당 미팅에 가입했는지)
         */
        List<Charge> charges = new ArrayList<Charge>();
        charges.add(chargeRepository.findById(chargeId).orElseThrow(ChargeNotFoundException::new));
        return chargeConverter.toChargeReses(charges);
    }

    @Transactional(readOnly = true)
    public List<ChargeRes> getPayCharges(Long meetId, CustomUserDetails userDetails){

        if(!meetRepository.existsById(meetId)){

            throw new MeetNotFoundException();
        }

        /*
            해당 미팅에 대한 권한이 있는지 체크(해당 미팅에 가입했는지)
         */

        User user = userRepository.findByUserId(userDetails.getUserId());
        Meet meet = meetRepository.getReferenceById(meetId);
        MeetMember meetMember = meetMemberRepository.findByUserAndMeet(user, meet).orElseThrow(MeetNotFoundException::new);
        List<Charge> charges = chargeRepository.findByMeetId(meetId);

        List<Charge> chargesRes = new ArrayList<Charge>();
        for(Charge charge : charges){

            List<ChargeMember> chargeMembers = charge.getChargeMembers();
            for(ChargeMember chargeMember : chargeMembers){

                if (meetMember.getId() == chargeMember.getPayer().getId()) {

                    chargesRes.add(charge);
                    break;
                }
            }
        }

        return chargeConverter.toChargeReses(chargesRes);
    }

    @Transactional
    public void deleteCharge(Long meetId, Long chargeId) {

        Meet meet = meetRepository.findById(meetId)
            .orElseThrow(MeetNotFoundException::new);


        /*
            해당 미팅에 대한 권한이 있는지 체크
         */
        /*
            해당 정산에 대한 권한이 있는지 체크
         */

        System.out.println("deleted charge " + chargeId);
        chargeRepository.deleteById(chargeId);
    }

}
