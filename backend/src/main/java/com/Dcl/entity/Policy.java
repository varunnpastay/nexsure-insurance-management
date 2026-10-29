package com.Dcl.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Policy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer policyId;
	
	private String policyName;
	
	private Double premiumAmount;
	
	private Double coverageAmount;
	
	private Integer durationMonths;
	
	@Column(length = 1000)
	private String description;
	
	private String status; // ACTIVE / INACTIVE
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private PolicyCategory category;
}
