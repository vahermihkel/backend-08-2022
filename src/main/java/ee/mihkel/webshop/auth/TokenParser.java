package ee.mihkel.webshop.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

        // SIIN SAAN TOKENI KÄTTE JA HAKKAN LAHTI PAKKIMA
        // JWT (Json Web Token)

        super.doFilterInternal(request, response, chain);
    }
}
