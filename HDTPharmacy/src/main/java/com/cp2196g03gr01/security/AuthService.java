package com.cp2196g03gr01.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cp2196g03gr01.entity.User;
import com.cp2196g03gr01.repository.IUserRepository;

@Service
public class AuthService implements UserDetailsService{

	@Autowired
	private IUserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User inDB = userRepository.findByUsername(username);
	    if (inDB == null) {
	      throw new UsernameNotFoundException("User not found");
	    }
	    return new UserDetails() {

	      @Override
	      public Collection<? extends GrantedAuthority> getAuthorities() {
	        return AuthorityUtils.createAuthorityList("user");
	      }

	      @Override
	      public String getPassword() {
	        return inDB.getPassword();
	      }

	      @Override
	      public String getUsername() {
	        return inDB.getUsername();
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
	        return true;
	      }
	      
	    };
	}

}
