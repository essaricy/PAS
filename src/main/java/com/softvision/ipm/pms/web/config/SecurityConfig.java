package com.softvision.ipm.pms.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.softvision.ipm.pms.acl.service.SVAuthenticationProvider;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration {
	
    @Autowired private SVAuthenticationProvider authenticationProvider;
    @Autowired private ApplicationContext applicationContext;
    @Autowired private PermissionEvaluator permissionEvaluator;

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
    } 

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Configuration
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    	@Override
        protected void configure(HttpSecurity http) throws Exception {
    		//http.csrf().disable();
    		http.csrf().disable()
    		.authorizeRequests().antMatchers("/", "/login").permitAll()
    				.anyRequest().authenticated()
    		.and()
                .formLogin()
                    .loginPage("/login")
                	.failureUrl("/")
                    .defaultSuccessUrl("/dashboard")
                    .usernameParameter("username")
    				.passwordParameter("password")
            .and()
                .logout()
    				.logoutSuccessUrl("/");
        }

    	@Override
    	public void configure(WebSecurity web) throws Exception {
    		web.ignoring().antMatchers("/fonts/**", "/scripts/**", "/images/**",
    				"/AdminBSBMaterialDesign/**");
    	}
    }
}