package com.swyp.mema.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter
@Table(name="users_2")
@NoArgsConstructor
public class UserEntity {

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
    private String puz_id;

    @Column(nullable = false)
    private String puz_color;

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
    public UserEntity(String email, String password, String nickname, String puz_id, String puz_color, String role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.puz_id = puz_id;
        if(puz_id == null) this.puz_id = "default_id";
        this.puz_color = puz_color;
        if(puz_color == null) this.puz_color = "default_color";
        this.role = role;
    }
}
