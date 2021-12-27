package org.mylearning.jwtexample;

import org.mylearning.jwtexample.security.RequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
//newly added
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    RequestFilter filter;

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * Configuring CORS filter
     *
     * @return - Cors filter Configurations
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * Configuring http security object (A Filter) to allow / deny API Calls with or without authorization
     *
     * @param http - HttpSecurity module
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // /health & /login APIs are open for all, but other api's need to be authenticated properly
        http.csrf().disable()
                .authorizeRequests().antMatchers("/health", "/login").permitAll()
                .antMatchers("/protected/**").authenticated()
                // configuring the spring security for our custom needs
                // Specifically configure the security to be stateless
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

}
