package henry.hotel.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import henry.hotel.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserService userService;

	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((requests) ->
						requests
								.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
								.requestMatchers("/home-page").permitAll()
								.requestMatchers("/", "/new-reservation", "/your-reservations").hasAnyRole("EMPLOYEE")
				)
				.formLogin((form) ->
						form
								.loginPage("/login")
								.loginProcessingUrl("/login")
								.successHandler(customAuthenticationSuccessHandler)
								.permitAll()
				)
				.logout((logout) ->
						logout.permitAll()
				)
				.exceptionHandling((exceptionHandling) ->
						exceptionHandling.accessDeniedPage("/access-denied")
				)
				.csrf((csrf) ->
						csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
				)
				.headers((headers) ->
						headers.disable()
				);

		return http.build();
	}
}
