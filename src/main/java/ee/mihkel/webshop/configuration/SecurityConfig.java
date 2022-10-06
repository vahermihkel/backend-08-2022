package ee.mihkel.webshop.configuration;

import ee.mihkel.webshop.auth.TokenParser;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);

        http
                .cors().and().headers().xssProtection().disable().and()
                .csrf().disable()
                .addFilter(new TokenParser(authenticationManager()))
                .authorizeRequests()
                .antMatchers("/active-products").permitAll()
                .antMatchers("/get-product/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/parcel-machines/**").permitAll()
                .antMatchers(HttpMethod.GET, "/category").permitAll()
                .antMatchers( "/cart-products/**").permitAll()
                .antMatchers("/persons").hasAuthority("admin")
                .antMatchers("/products").hasAuthority("admin")
                .antMatchers("/add-stock").hasAuthority("admin")
                .antMatchers("/decrease-stock").hasAuthority("admin")
                .antMatchers("/delete-product/**").hasAuthority("admin")
                .antMatchers(HttpMethod.POST, "category").hasAuthority("admin")
                .antMatchers(HttpMethod.DELETE, "category").hasAuthority("admin")
                .antMatchers(HttpMethod.POST, "add-product").hasAuthority("admin")
                .antMatchers("/check-if-admin").hasAuthority("admin")
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
}
