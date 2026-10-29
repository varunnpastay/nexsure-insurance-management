package com.Dcl.entity;

import java.time.LocalDate;
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
public class Claim {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer claimId;
	
	private Double claimAmount;
	
	@Column(length = 1000)
	private String claimReason;
	
	private LocalDate claimDate;
	
	private String status; // PENDING / APPROVED / REJECTED
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JoinColumn(name = "purchase_id")
	private Purchase purchase;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
