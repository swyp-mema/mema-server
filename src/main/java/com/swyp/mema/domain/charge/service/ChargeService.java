package com.swyp.mema.domain.charge.service;

import com.swyp.mema.domain.charge.converter.ChargeConverter;
import com.swyp.mema.domain.charge.dto.request.ChargeReq;
import com.swyp.mema.domain.charge.dto.response.ChargeRes;
import com.swyp.mema.domain.charge.model.Charge;
import com.swyp.mema.domain.charge.repository.ChargeRepository;
import com.swyp.mema.domain.meet.exception.MeetNotFoundException;
import com.swyp.mema.domain.meet.repository.MeetRepository;
import com.swyp.mema.domain.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargeService {

    private final ChargeRepository chargeRepository;
    private final ChargeConverter chargeConverter;
    private final MeetRepository meetRepository;

    @Transactional
    public void createCharge(Long meetId, ChargeReq req, CustomUserDetails userDetails) {

        // meet_id 검증
        if(!meetRepository.existsById(meetId)){

            throw new MeetNotFoundException();
        }
        chargeRepository.save(chargeConverter.generateCharge(meetId, req, userDetails.getUserId()));
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

    public void deleteCharge(Long meetId, Long chargeId) {

        if(!meetRepository.existsById(meetId)){

            throw new MeetNotFoundException();
        }

        /*
            해당 미팅에 대한 권한이 있는지 체크
         */
        /*
            해당 정산에 대한 권한이 있는지 체크
         */

        chargeRepository.deleteById(chargeId);
    }

}
