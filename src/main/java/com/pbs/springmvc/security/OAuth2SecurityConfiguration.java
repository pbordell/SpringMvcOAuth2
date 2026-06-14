package com.pbs.springmvc.security;

import com.pbs.springmvc.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableWebSecurity
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
    }

    @Override
	protected void configure(HttpSecurity http) throws Exception {
		http.anonymous()
			.disable()
			.authorizeRequests()
			.antMatchers("/oauth/token")
			.permitAll()
			.and()
				// se comentan para permitir HTTP estándar, ya que sino habría que configurar un conector SSL
				// en el archivo server.xml de tu instalación de Tomcat mediante un certificado autofirmado (Keystore).
			//.requiresChannel()
			//.anyRequest()
			//.requiresSecure()
			//.and()
			.csrf().disable();
	}

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new PlatformTransactionManager() {
			@Override
			public org.springframework.transaction.TransactionStatus getTransaction(org.springframework.transaction.TransactionDefinition definition) throws org.springframework.transaction.TransactionException {
				return new org.springframework.transaction.support.SimpleTransactionStatus();
			}
			@Override
			public void commit(org.springframework.transaction.TransactionStatus status) throws org.springframework.transaction.TransactionException {}
			@Override
			public void rollback(org.springframework.transaction.TransactionStatus status) throws org.springframework.transaction.TransactionException {}
		};
	}

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Bean
	@Autowired
	public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
		TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
		handler.setTokenStore(tokenStore);
		handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
		handler.setClientDetailsService(clientDetailsService);
		return handler;
	}
	
	@Bean
	@Autowired
	public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
		TokenApprovalStore store = new TokenApprovalStore();
		store.setTokenStore(tokenStore);
		return store;
	}

}
