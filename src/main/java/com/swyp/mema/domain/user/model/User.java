package com.swyp.mema.domain.user.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String puzId;

    @Column(nullable = false)
    private String puzColor;

    @Column(nullable = false)
    private String role;

    public void setUsername(String username) {

        if(username == null || username.equals("")) return;
        this.userId = Long.parseLong(username);
    }
    public String getUsername() {
        return String.valueOf(userId);
    }

    @Builder
    public User(String email, String password, String nickname, String puzId, String puzColor, String role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.puzId = puzId;
        if(puzId == null) this.puzId = "default_id";
        this.puzColor = puzColor;
        if(puzColor == null) this.puzColor = "default_color";
        this.role = role;
    }
}
