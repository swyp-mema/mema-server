package com.swyp.mema.domain.badge.model;

import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class Badge extends BaseEntity {

    // 유저 아이디와 동일
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long badgeId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 미팅 생성 횟수
    @Column(nullable = false)
    private int meetCreateCount;

    //장소 투표 횟수
    @Column(nullable = false)
    private int locationVoteCount;

    @Builder
    public Badge(User user) {

        this.user = user;
        meetCreateCount = 0;
        locationVoteCount = 0;
    }

    /**
     * 미팅 생성시 생성횟수 증가
     */
    public void createMeet(){

        this.meetCreateCount++;
    }

    /**
     * 장소 투표  시 투표수 증가
     */
    public void createLocationVote(){

        this.locationVoteCount++;
    }

    public boolean badge1(){

        return true;
    }

    // 첫 미팅 생성 뱃지 조회
    public boolean badge2(){

        return this.meetCreateCount >= 1;
    }

    // 메마 방문 5회
    public boolean badge3(){

        return user.getVisitCount() >= 5;
    }

    // 메마 방문 20회
    public boolean badge4(){

        return user.getVisitCount() >= 20;
    }

    // 미팅 3회 생성
    public boolean badge5(){

        return this.meetCreateCount >= 3;
    }

    // 미팅 5회 생성
    public boolean badge6(){

        return this.meetCreateCount >= 5;
    }

    // 출발위치 3회 입력
    public boolean badge7(){

        return false;
    }

    // 출발위치 5회 입력
    public boolean badge8(){

        return false;
    }

    // 정산 3회 이용
    public boolean badge9(){

        return false;
    }

    // 정산 10회 이용
    public boolean badge10(){

        return false;
    }

    // 미팅 10회 생성
    public boolean badge11(){

        return false;
    }

    // 메마 방문 50회
    public boolean badge12(){

        return false;
    }

    /**
     * 모든 뱃지 상태를 조회하는 메서드
     *
     * @return Map<String, Boolean> : 각 뱃지 상태
     */
    public ArrayList<Boolean> getAllBadges() {
        ArrayList<Boolean> badgeStatus = new ArrayList<>();

        badgeStatus.add(badge1());
        badgeStatus.add(badge2());
        badgeStatus.add(badge3());
        badgeStatus.add(badge4());
        badgeStatus.add(badge5());
        badgeStatus.add(badge6());
        badgeStatus.add(badge7());
        badgeStatus.add(badge8());
        badgeStatus.add(badge9());
        badgeStatus.add(badge10());
        badgeStatus.add(badge11());
        badgeStatus.add(badge12());

        return badgeStatus;
    }

}
