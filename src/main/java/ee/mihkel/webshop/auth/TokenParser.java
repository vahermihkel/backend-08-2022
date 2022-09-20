package ee.mihkel.webshop.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TokenParser extends BasicAuthenticationFilter {
    public TokenParser(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    // doFil
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // GET orders      jksdnfasjdnqwjenqwjheqwe.qweqwjeniqwenqw.qwenqweqwie.ejqweinqweqnweij
        // 1. Vaatab kas Token on üldse olemas
        // 2. Kas Token on üldse minu oma
        // 3. Kas Token on mitteaegunud
        // 4. Võtan Tokeni küljest kasutaja
        // 5. Panen ta globaalselt tervele rakendusele et see inimene teeb päringut

        // getOrders() {
        // VÕTA GLOBAALNE SISSELOGITU --> saan ta kätte ja seejärel saan ta tellimused kätte
        // Kui päring lõppeb (tagastatakse front-endile), siis see inimene pole enam globaalselt sees

        System.out.println("KONTROLLIN TOKENIT");

        System.out.println(request.getMethod());

        System.out.println(request.getRequestURI());

        System.out.println(request.getHeader("Authorization"));

        // Bearer dasdaseqwe31231qead
        String headerToken = request.getHeader("Authorization");
        if (headerToken != null && headerToken.startsWith("Bearer ")) {
            String token = headerToken.replace("Bearer ", "");

            System.out.println("TOKEN: " + token);

            Claims claims = Jwts.parser()
                    .setSigningKey("absolutely-secret-key")
                    .parseClaimsJws(token)
                    .getBody();

            String issuer = claims.getIssuer();

            System.out.println("ISSUER: " + issuer);

            String personCode = claims.getSubject();

            System.out.println(personCode);

//            System.out.println(claims.get("role"));

            List<GrantedAuthority> authorities = null;
            if (claims.getId() != null && claims.getId().equals("admin")) {
                GrantedAuthority authority = new SimpleGrantedAuthority("admin");
                authorities = new ArrayList<>(Collections.singletonList(authority));
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    personCode,null, authorities
            );

            // turva globaalne hoidja
            //SecurityContextHolder.getContext().getAuthentication().getPrincipal() --> isikukood
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // MalformedJwtException
            // ExpiredJwtException
        }

        super.doFilterInternal(request, response, chain);
    }
}
