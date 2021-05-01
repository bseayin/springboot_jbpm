package com.config;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.service.UserService;


@Configuration("kieServerSecurity")
@EnableWebSecurity
public class DefaultWebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserService userService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
        //使用BCryptPasswordEncoder对密码进行编码，所有用户密码均为123456
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //编码后为 $2a$10$ZxaV8wD2gDSLokqVK9lJW.O9qqK07LEkcKV6KaUtCSZk0EqI2pXyO
		System.out.println("123456 密码编码: "+passwordEncoder.encode("123456"));
		return passwordEncoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.cors().and().csrf().disable().authorizeRequests().antMatchers("/kpi/index","/kpi/error").permitAll().antMatchers("/kpi/**")
		//.hasRole("Administrators")
		.authenticated()
		.and().exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
			
			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException arg2)
					throws IOException, ServletException {
					response.sendRedirect("/spring/kpi/error");
			}
		}).and().httpBasic()
				.and().headers().frameOptions().disable().and().formLogin().usernameParameter("username").passwordParameter("password").loginProcessingUrl("/login").and()
				.logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler() {
					
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication arg2)
							throws IOException, ServletException {
						response.sendRedirect("/spring/kpi/index");
						
					}
				});
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}
}