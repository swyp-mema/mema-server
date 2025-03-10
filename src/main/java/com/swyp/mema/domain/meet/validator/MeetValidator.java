package com.swyp.mema.domain.meet.validator;

import com.swyp.mema.domain.meet.exception.JoinCodeInvalidException;
import com.swyp.mema.domain.meet.exception.MaxActiveMeetsExceededException;
import com.swyp.mema.domain.meet.exception.MeetNotFoundException;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.repository.MeetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetValidator {

    private final MeetRepository meetRepository;

    // ID 기반 약속 존재 여부 검증
    public Meet validateMeetExists(Long meetId) {
        return meetRepository.findById(meetId)
                .orElseThrow(MeetNotFoundException::new);
    }

    // 참여 코드로 약속 존재 여부 검증
    public Meet validateMeetExistsByCode(int joinCode) {
        return meetRepository.findByCode(joinCode)
                .orElseThrow(JoinCodeInvalidException::new);
    }

    // 사용자의 진행 중인 약속 개수 검증
    public void validateUserCanCreateMoreMeets(Long userId) {
        Long activeMeetCount = meetRepository.countActiveMeetsByUserId(userId);
        if (activeMeetCount > 4) {
            throw new MaxActiveMeetsExceededException();
        }
    }
}
