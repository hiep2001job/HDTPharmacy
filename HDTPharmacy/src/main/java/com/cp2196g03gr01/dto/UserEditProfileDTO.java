package com.cp2196g03gr01.dto;

import lombok.Data;

@Data
public class UserEditProfileDTO {
	private Long id;
	private String userFullName;
	private String phone;
	private String address;
}
