 package com.cp2196g03gr01.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.cp2196g03gr01.common.RolesEnum;

import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long id;
	
	@Column(name = "role_name")
	@Enumerated(EnumType.STRING)
	private RolesEnum name;
	
	@Column(name = "role_description")
	private String description;
	
}
