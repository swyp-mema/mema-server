package com.swyp.mema.global.validation;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.validator.MeetValidator;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.validator.MeetMemberValidator;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationFacade {

    private final UserValidator userValidator;
    private final MeetValidator meetValidator;
    private final MeetMemberValidator meetMemberValidator;

    /**
     * User 기반 Validate
     */
    // userId 기반 유저 존재 여부 검증
    public User validateUserExists(Long userId) {
        return userValidator.validateExists(userId);
    }

    //  이메일 중복 검증
    public void checkEmailNotExists(String email) {
        userValidator.checkEmailNotExists(email);
    }

    // 이메일을 통한 유저 조회
    public User findByEmail(String email) {
        return userValidator.findByEmail(email);
    }

    /**
     * Meet 관련 Validate
     */
    // 약속 존재 여부 검증
    public Meet validateMeetExists(Long meetId) {
        return meetValidator.validateMeetExists(meetId);
    }

    // 참여 코드로 약속 존재 여부 검증 후 Meet 반환
    public Meet validateMeetExistsByCode(int joinCode) {
        return meetValidator.validateMeetExistsByCode(joinCode);
    }

    // 참여 코드의 중복 여부 검증
    public boolean validateMeetCodeDuplicate(int code) {
        return meetValidator.isMeetCodeDuplicate(code);
    }

    // 사용자의 진행 중인 약속 개수 검증
    public void validateUserCanCreateMoreMeets(Long userId) {
        meetValidator.validateUserCanCreateMoreMeets(userId);
    }


    /**
     * MeetMeber 관련 Validate
     */
    // 사용자가 해당 약속의 약속원인지 검증
    public MeetMember validateUserIsMeetMember(User user, Meet meet) {
        return meetMemberValidator.validateUserIsMeetMember(user, meet);
    }

    // 사용자가 이미 해당 약속에 등록되어 있는지 검증
    public void validateUserNotAlreadyInMeet(Meet meet, User user) {
        meetMemberValidator.validateUserNotAlreadyInMeet(meet, user);
    }
}
