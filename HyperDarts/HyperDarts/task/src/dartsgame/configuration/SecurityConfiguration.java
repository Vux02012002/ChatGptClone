package dartsgame.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())
                .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager
                .createUser(
                        User
                                .withUsername("ivanhoe@acme.com")
                                .password("{noop}oMoa3VvqnLxW")
                                .roles("GAMER")
                                .build()
                );
        manager
                .createUser(
                        User
                                .withUsername("robinhood@acme.com")
                                .password("{noop}ai0y9bMvyF6G")
                                .roles("GAMER")
                                .build()
                );
        manager
                .createUser(
                        User
                                .withUsername("wilhelmtell@acme.com")
                                .password("{noop}bv0y9bMvyF7E")
                                .roles("GAMER")
                                .build()
                );
        manager
                .createUser(
                        User
                                .withUsername("admin@acme.com")
                                .password("{noop}zy0y3bMvyA6T")
                                .roles("ADMIN")
                                .build()
                );

        return manager;
    }
}
