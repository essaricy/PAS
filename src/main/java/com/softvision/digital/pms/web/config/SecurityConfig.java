package com.softvision.digital.pms.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;

import com.softvision.digital.pms.auth.service.SVAuthenticationProvider;
import com.softvision.digital.pms.common.constants.AuthorizeConstant;

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

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		log.info("auth::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::"); 
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
			.antMatchers("/admin/template/**").access(AuthorizeConstant.IS_MANAGER_OR_ADMIN)
			.antMatchers("/admin/**").access(AuthorizeConstant.IS_ADMIN)
			.antMatchers("/manager/**").access(AuthorizeConstant.IS_MANAGER)
			.antMatchers("/employee/**").access(AuthorizeConstant.IS_EMPLOYEE)
			.antMatchers("/support/**").access(AuthorizeConstant.IS_SUPPORT)
			.anyRequest().fullyAuthenticated()
		.and()
			.exceptionHandling().accessDeniedPage("/403");

		http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry);
    }

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/fonts/**", "/scripts/**", "/images/**", "/AdminBSBMaterialDesign/**");
	}

}