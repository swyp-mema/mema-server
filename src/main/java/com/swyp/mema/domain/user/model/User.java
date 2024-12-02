package com.swyp.mema.domain.user.model;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.swyp.mema.global.base.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY) // Auto Increment
	private Long userId;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Long puzId;

	@Column(nullable = false)
	private String puzColor;

	@Builder
	public User(String nickname, String email, String password, Long puzId, String puzColor) {
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.puzId = puzId;
		this.puzColor = puzColor;
	}
}
