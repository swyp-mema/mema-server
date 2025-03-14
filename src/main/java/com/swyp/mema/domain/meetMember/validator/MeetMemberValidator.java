package com.swyp.mema.domain.meetMember.validator;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meetMember.exception.MeetMemberNotFoundException;
import com.swyp.mema.domain.meetMember.exception.NotMeetMemberException;
import com.swyp.mema.domain.meetMember.exception.UserAlreadyRegisteredException;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.global.validation.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetMemberValidator implements EntityValidator<MeetMember, Long> {

    private final MeetMemberRepository meetMemberRepository;

    @Override
    public MeetMember validateExists(Long meetMemberId) {
        return meetMemberRepository.findById(meetMemberId)
                .orElseThrow(MeetMemberNotFoundException::new);
    }

    // 사용자가 특정 약속의 약속원인지 검증
    public MeetMember validateUserIsMeetMember(User user, Meet meet) {
        return meetMemberRepository.findByUserAndMeet(user, meet)
                .orElseThrow(NotMeetMemberException::new);
    }

    // 사용자가 이미 해당 약속에 등록되어 있는지 검증
    public void validateUserNotAlreadyInMeet(Meet meet, User user) {
        if (meetMemberRepository.existsByMeetAndUser(meet, user)) {
            throw new UserAlreadyRegisteredException();
        }
    }

    // 사용자의 약속원 정보가 해당 약속과 일치하는지 검증
    public void validateMeetMemberBelongToMeet(MeetMember meetMember, Long meetId) {
        if (!meetMember.getMeet().getId().equals(meetId)) {
            throw new NotMeetMemberException();
        }
    }

}
