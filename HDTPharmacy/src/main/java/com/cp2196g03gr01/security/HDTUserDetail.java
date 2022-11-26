package com.cp2196g03gr01.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cp2196g03gr01.entity.Role;
import com.cp2196g03gr01.entity.User;

public class HDTUserDetail implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;

	public HDTUserDetail(User user) {
		this.user = user;

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName().toString()));
		}

		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.getIsEnable();
	}

	public String getFullname() {
		return user.getUserFullname();
	}

	public Long getId() {
		return user.getId();
	}

	public String getRole() {
		return user.getAllRole();
	}

	public String getAvatar() {
		return user.getImage();
	}
	
	public void refreshUserDetail(User updatedUser) {
		user.setAddress(updatedUser.getEmail());
		user.setPhone(updatedUser.getPhone());
		user.setImage(updatedUser.getImage());
		user.setUserFullname(updatedUser.getUserFullname());
	}
	
	

}
