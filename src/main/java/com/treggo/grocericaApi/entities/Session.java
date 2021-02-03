package com.treggo.grocericaApi.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sessionId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String token;

	@Column
	private String userType;

	@Column(nullable = false)
	private LocalDate created_on;

	public Session() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Session(Long sessionId, Long userId, String token, String userType, LocalDate created_on) {
		super();
		this.sessionId = sessionId;
		this.userId = userId;
		this.token = token;
		this.userType = userType;
		this.created_on = created_on;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDate getCreated_on() {
		return created_on;
	}

	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}
