package com.cp2196g03gr01.entity;

import java.io.Serializable;
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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.JoinColumn;

import lombok.Data;

@Entity
@Data
public class User  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9124251877298804035L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "user_username", length = 100)
	private String userFullname="";

	@JsonIgnore
	@Column(name = "user_password", length = 300)
	private String password="";
	
	
	@Column(name = "user_email", length = 100,unique = true)
	private String email="";
	
	@Column(name = "user_address", length = 100)
	private String address="";

	@Column(name = "user_phone", length = 20)
	private String phone="";
	
	@Column(name="user_image")
    private String image;

	@JsonIgnore
	@Column(name = "user_reset_password_token", length = 30)
	private String resetPasswordToken="";

	@Column(name = "user_enable")
	private Boolean isEnable = true;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	Set<Role> roles = new HashSet<Role>();

	 @Transient
		public String getPhotoPath() {
			return "/user-images/" + this.image;
		}	
	
	@Transient
	public String getAllRole() {
		String rolesName = "";
		for (Role role : roles) {
			rolesName += " " + role.getName().getValue();
		}
		return rolesName.trim();
	}
}
