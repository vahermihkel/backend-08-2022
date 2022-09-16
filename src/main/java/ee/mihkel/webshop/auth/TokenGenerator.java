package ee.mihkel.webshop.auth;

import ee.mihkel.webshop.controller.model.TokenResponse;
import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {


    public TokenResponse generateNewToken(String personCode) {
        // VALIME MILLE ABIL KÄIB ALLKIRJASTAMINE
        // SHA512
        // Paneme secret parooli
        // Isikukood
        // Expiration
        String token = "adsadsa " + personCode;
        // TEEN JSONWEBTOKEN DEPENDENCY abil uue tokeni
        // JA TAGASTAN
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(token);
        return tokenResponse;

    }
}
