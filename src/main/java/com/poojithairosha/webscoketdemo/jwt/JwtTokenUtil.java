package com.poojithairosha.webscoketdemo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private Long EXPIRATION;

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Map.of());
    }

    public String generateToken(UserDetails userDetails, Map<String, String> extraClaims) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTCreator.Builder jwtBuilder = JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuer("poojitharirosha")
                .withClaim("role", userDetails.getAuthorities().stream().filter(a -> a.getAuthority().startsWith("ROLE_")).findFirst().orElseThrow().getAuthority())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION));
        extraClaims.forEach(jwtBuilder::withClaim);

        return jwtBuilder.sign(algorithm);
    }

    public DecodedJWT decodeJWT(String token) throws TokenExpiredException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public <T> T getClaim(String token, Function<Map<String, Claim>, T> claimsResolver) {
        Map<String, Claim> claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getUsername(String token) {
        return getClaim(token, claims -> claims.get("sub").asString());
    }

    public Date getExpiration(String token) {
        return getClaim(token, claims -> claims.get("exp").asDate());
    }

    public Map<String, Claim> getAllClaims(String token) {
        return decodeJWT(token).getClaims();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            return decodeJWT(token).getSubject().equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (TokenExpiredException ex) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return decodeJWT(token).getExpiresAt().before(new Date(System.currentTimeMillis()));
        } catch (TokenExpiredException ex) {
            return true;
        }
    }

}
