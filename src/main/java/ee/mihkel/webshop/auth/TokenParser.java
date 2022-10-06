package ee.mihkel.webshop.auth;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
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
import java.util.Collections;
import java.util.List;

@Log4j2
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



        // Bearer dasdaseqwe31231qead
        String headerToken = request.getHeader("Authorization");
        if (headerToken != null && headerToken.startsWith("Bearer ")) {
            String token = headerToken.replace("Bearer ", "");

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey("absolutely-secret-key")
                        .parseClaimsJws(token)
                        .getBody();

                String personCode = claims.getSubject();

                log.info("Successfully logged in {}", personCode);
                log.info("Checking if {}", claims.getAudience());

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
            } catch (ExpiredJwtException e) {
                log.error("QUERY WITH EXPIRED TOKEN {}", token);
            } catch (UnsupportedJwtException e) {
                log.error("QUERY WITH UNSUPPORTED JWT TOKEN {}", token);
            } catch (MalformedJwtException e) {
                log.error("QUERY WITH MALFORMED TOKEN {}", token);
            } catch (SignatureException e) {
                log.error("QUERY WITH FALSE SIGNATURE TOKEN {}", token);
            } catch (IllegalArgumentException e) {
                log.error("QUERY WITH ILLEGAL ARGUMENT TOKEN {}", token);
            }
        }

        super.doFilterInternal(request, response, chain);
    }
}
