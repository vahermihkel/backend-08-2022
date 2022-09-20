package ee.mihkel.webshop.auth;

import ee.mihkel.webshop.controller.model.TokenResponse;
import ee.mihkel.webshop.entity.Person;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenGenerator {


    public TokenResponse generateNewToken(Person person) {
        // VALIME MILLE ABIL KÄIB ALLKIRJASTAMINE
        // SHA512
        // Paneme secret parooli
        // Isikukood
        // Expiration
//        String token = "adsadsa " + personCode;

        // Jwts.build
        // Subject, Expiration, Issuer, Id, IssuedAt, Audience, Header, NotBefore, Payload

        Date expirationDate = DateUtils.addHours(new Date(), 4);

//        Map<String, Object> claims = new HashMap<>();
//        claims.put("role", person.getRole());

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, "absolutely-secret-key")
                .setExpiration(expirationDate)
                .setIssuer("mihkli-webshop")
                .setSubject(person.getPersonCode())
                .setId(person.getRole())
//                .setClaims(claims)
                .compact();

        // TEEN JSONWEBTOKEN DEPENDENCY abil uue tokeni
        // JA TAGASTAN
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(token);
        return tokenResponse;

    }
}
