package com.softvision.ipm.pms.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;

import com.softvision.ipm.pms.acl.service.SVAuthenticationProvider;
import com.softvision.ipm.pms.common.constants.AuthorizeConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private SessionRegistry sessionRegistry;

	@Autowired
	private SVAuthenticationProvider authenticationProvider;

	//@Autowired private GoogleReCAPTCHAFilter reCAPTCHAFilter;

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		log.info("auth::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::"); 
		//http.addFilterBefore(reCAPTCHAFilter, BasicAuthenticationFilter.class);

		http.csrf().and().authenticationProvider(authenticationProvider);
		http.csrf().disable()
            .formLogin()
                .loginPage("/login")
            	.failureUrl("/")
                .defaultSuccessUrl("/dashboard")
                .usernameParameter("username")
				.passwordParameter("password")
        .and()
            .logout()
				.logoutSuccessUrl("/")
	    .and().authorizeRequests()
			.antMatchers("/", "/login").permitAll()
			.antMatchers("/admin/**").access(AuthorizeConstant.IS_ADMIN)
			.anyRequest().fullyAuthenticated()
		.and().exceptionHandling().accessDeniedPage("/403");

		http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry);
    }

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/fonts/**", "/scripts/**", "/images/**", "/AdminBSBMaterialDesign/**");
	}

}