package com.ptff.qsystem.config;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);


    @Resource
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers("*").permitAll()
		.antMatchers(HttpMethod.OPTIONS).permitAll();
        
    	http
	        .formLogin()
		        .loginPage("/login.html")
		        .failureUrl("/login.html")
		        .defaultSuccessUrl("/dashboard", true)
    		.and()
    			.logout()
    			.logoutSuccessUrl("/login.html")
    		.and()
    			.authorizeRequests()
                	.antMatchers( "/login.html", "/js/**", "/", "/css/**", "/fonts/**", "/img/**", "/sound/**")
                		.permitAll()
                	.anyRequest()
                		.authenticated();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}