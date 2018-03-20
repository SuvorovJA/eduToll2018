package edu.server.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home").authenticated()
                .antMatchers("/css/**", "/img/**").permitAll()
                .antMatchers("/routes/**", "/payments/**").hasRole("CLIENT")
                .antMatchers("/registerClient/**").hasRole("MANAGER")
                .antMatchers("/registerManager/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().permitAll();
    } //.anyRequest().authenticated() // or .anyRequest().hasRole("CLIENT")?

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("client").password("client").roles("CLIENT")
                .and()
                .withUser("manager").password("manager").roles("MANAGER", "CLIENT")
                .and()
                .withUser("root").password("root").roles("ADMIN", "MANAGER", "CLIENT");
    }
}
