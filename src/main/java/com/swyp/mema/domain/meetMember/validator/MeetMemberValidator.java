package com.swyp.mema.domain.meetMember.validator;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meetMember.exception.NotMeetMemberException;
import com.swyp.mema.domain.meetMember.exception.UserAlreadyRegisteredException;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetMemberValidator {

    private final MeetMemberRepository meetMemberRepository;

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
}
