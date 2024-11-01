package com.travelweb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
	
	@Id //not null & primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id tự tăng dần
	private Long id;
	
	@Column(name = "createdBy")
	@CreatedBy
	private String createdBy;
	
	@Column
	@CreatedDate
	private Date createdDates;
	
	@Column
	@LastModifiedBy
	private String modifiedBy;
	
	@Column
	@LastModifiedDate
	private Date modifiedDates;
	
	public Long getId() {
		return id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDates() {
		return createdDates;
	}

	public void setCreatedDates(Date createdDates) {
		this.createdDates = createdDates;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDates() {
		return modifiedDates;
	}

	public void setModifiedDates(Date modifiedDates) {
		this.modifiedDates = modifiedDates;
	}
	
	
	
}
