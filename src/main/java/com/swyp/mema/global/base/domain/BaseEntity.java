package com.swyp.mema.global.base.domain;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

	@CreatedDate
	@Column(name = "create_date", updatable = false, nullable = false)
	private LocalDateTime createDate;

	@LastModifiedDate
	@Column(name = "update_date", nullable = false)
	private LocalDateTime updateDate;
}

