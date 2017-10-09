package com.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class AppSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable().and() // fix to get h2-console
														// working.
				.authorizeRequests().antMatchers("/v1/**", "/v2/**", "/swagger-ui/**", "/api-docs/**", "/h2/**")
				.permitAll() // we don't want Sprint to protect these urls
				.antMatchers("/products/**", "/bills/**").authenticated().and().httpBasic().realmName("checkoutstore").and()
				.csrf().disable();
	}

	@Autowired
	public void configureInMemoryUsers(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("root").password("root").roles("ADMIN", "USER");
	}

}
