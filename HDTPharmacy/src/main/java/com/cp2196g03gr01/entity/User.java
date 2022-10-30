package com.cp2196g03gr01.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.JoinColumn;

import lombok.Data;

@Entity
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "user_username", length = 100)
	private String userFullname;

	@JsonIgnore
	@Column(name = "user_password", length = 300)
	private String password;

	@Column(name = "user_email", length = 100)
	private String email;

	@Column(name = "user_phone", length = 20)
	private String phone;
	
	@JsonIgnore
	@Column(name = "user_reset_password_token", length = 30)
	private String resetPasswordToken;
	
	@Column(name = "user_enable")
	private Boolean isEnable;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	Set<Role> roles = new HashSet<Role>();
}
