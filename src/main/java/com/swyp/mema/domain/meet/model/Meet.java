package com.swyp.mema.domain.meet.model;

import static jakarta.persistence.GenerationType.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Meet {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column
	private String name;

	public Meet(String name) {
		this.name = name;
	}
}
