package com.swyp.mema.domain.voteLocation.validator;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.voteLocation.exception.DuplicateLocationVoteException;
import com.swyp.mema.domain.voteLocation.exception.LocationNotFoundException;
import com.swyp.mema.domain.voteLocation.model.Location;
import com.swyp.mema.domain.voteLocation.repository.LocationRepository;
import com.swyp.mema.global.validation.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationValidator implements EntityValidator<Location, Long> {

    private final LocationRepository locationRepository;

    @Override
    public Location validateExists(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(LocationNotFoundException::new);
    }

    // 사용자가 이미 위치 투표를 했는지 검증
    public void validateUserHasNotVoted(User user, Meet meet) {
        if (locationRepository.findByUserAndMeet(user, meet).isPresent()) {
            throw new DuplicateLocationVoteException();
        }
    }

    // 사용자의 위치 투표가 존재하는지 검증
    public Location validateUserLocationExists(User user, Meet meet) {
        return locationRepository.findByUserAndMeet(user, meet)
                .orElseThrow(LocationNotFoundException::new);
    }

}