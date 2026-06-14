package com.pbs.springmvc.security;

import com.pbs.springmvc.evaluators.PermisosMetodesEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;


@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "my_rest_api";

	@Autowired
	private PermisosMetodesEvaluator permisosMetodesEvaluator;
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);

		// 2. Creamos el manejador de expresiones específico para el servidor de recursos
		OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
		expressionHandler.setPermissionEvaluator(permisosMetodesEvaluator);

		// 3. Forzamos a los tokens Bearer a pasar por tu evaluador de base de datos
		resources.expressionHandler(expressionHandler);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				// 1. Deshabilitamos los accesos anónimos
				.anonymous().disable()

				// 2. Apagamos la protección CSRF para permitir POST, PUT y DELETE desde Postman
				.csrf().disable()

				// 3. Procesamos todas las rutas de la aplicación
				.requestMatchers().antMatchers("/**")

				.and()
				.authorizeRequests()

				// 4. Exigimos únicamente que el token Bearer sea válido para entrar.
				.antMatchers("/**").authenticated()

				.and()
				.exceptionHandling()
				.accessDeniedHandler(new org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler());
	}

}