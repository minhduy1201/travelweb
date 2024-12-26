package com.travelweb.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.travelweb.enumEntity.ERole;

import lombok.Data;

@Entity
@Table(name = "role")
public class RoleEntity extends BaseEntity {
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ERole name;

	public RoleEntity() {

	}

	public RoleEntity(ERole name) {
		this.name = name;
	}

	public ERole getName() {
		return name;
	}

	public void setName(ERole name) {
		this.name = name;
	}
	
	
}
