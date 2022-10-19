package com.cp2196g03gr01.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;

@Entity
@Data
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long id;
	
	@Column(name = "role_name")
	private String name;
	
	@Column(name = "role_description")
	private String description;
	
	@ManyToMany(mappedBy = "playingRoles")
	Set<User> users;
}
