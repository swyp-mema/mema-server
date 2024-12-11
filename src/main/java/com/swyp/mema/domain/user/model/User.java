package com.swyp.mema.domain.user.model;

import com.swyp.mema.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false)
    private String nickname;

    @Setter
    @Column(nullable = false)
    private Long puzId;

    @Setter
    @Column(nullable = false)
    private String puzColor;

    @Column(nullable = false)
    private String role;

    //모임 참여 횟수
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private Integer visitCount;

    public void setUsername(String username) {

        if(username == null || username.isEmpty()) return;
        this.userId = Long.parseLong(username);
    }
    public String getUsername() {
        return String.valueOf(userId);
    }

    @Builder
    public User(String email, String password, String nickname, Long puzId, String puzColor, String role, Integer visitCount) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.puzId = puzId;
        if(puzId == null) this.puzId = 0L;
        this.puzColor = puzColor;
        if(puzColor == null) this.puzColor = "default_color";
        this.role = role;
        this.visitCount = visitCount;
        if(visitCount == null || visitCount == 0) this.visitCount = 1;
    }
}
