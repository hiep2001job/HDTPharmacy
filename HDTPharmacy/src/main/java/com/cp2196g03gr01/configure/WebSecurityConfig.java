package com.cp2196g03gr01.configure;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.cp2196g03gr01.security.AuthService;

import groovyjarjarantlr4.v4.runtime.atn.SemanticContext.AND;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public AuthService userDetailsService() {
		return new AuthService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
		dao.setUserDetailsService(userDetailsService());
		dao.setPasswordEncoder(passwordEncoder());
		dao.setHideUserNotFoundExceptions(false);
		return dao;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/manage/employee/**", "/manage/all-retail/**", "/manage/category/**",
						"/manage/product/**","/manage/customer/**")
				.hasAuthority("MANAGER")
				
				.antMatchers("/retail/**","/profile/**").hasAnyAuthority("MANAGER", "EMPLOYEE")
				
				.anyRequest().authenticated()

				.and().formLogin().loginPage("/login/employee").usernameParameter("email").permitAll()
				.defaultSuccessUrl("/").failureUrl("/login/employee")

				.and().exceptionHandling().accessDeniedPage("/access-denied")

				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login/employee").deleteCookies("JSESSIONID").invalidateHttpSession(true)

				.and().logout().permitAll().and().rememberMe().key("EiRoqkIE1pnQmWEPKjG8_1123003381")
				.tokenValiditySeconds(7 * 24 * 60 * 60);

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webjars/**", "/admin/**", "/manage/employee/emailExist/**",
				"/forgot_password/employee", "/api/customer/**");
	}

}
