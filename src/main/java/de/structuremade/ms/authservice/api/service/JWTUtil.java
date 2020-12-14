package de.structuremade.ms.authservice.api.service;

import de.structuremade.ms.authservice.database.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTUtil {
    private final List<String> blacklistMap = new ArrayList<>();
    private static final String SECRET = "8NYjXn89%q4fbF5yTAEa4r_A^WeXq*gCtDe4!4mV-59SrJN%d!$4Qz*+";
    Key signingKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(SECRET), SignatureAlgorithm.HS512.getJcaName());

    @Bean
    private void blacklistedTokenSort() {
        if (!blacklistMap.isEmpty()) {
            for (int i = 0; i <= blacklistMap.size(); i++) {
                if (extractExpiration(blacklistMap.get(i)).before(new Date())) {
                    blacklistMap.remove(blacklistMap.get(i));
                }
            }
        }
    }

    public boolean isTokenInBlacklist(String token) {
        return blacklistMap.contains(token);
    }

    public void addBlacklistedToken(String token) {
        blacklistMap.add(token);
    }

    public String extractIdOrEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String createPasswordResetToken(User user) {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.DATE, 1);
        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration.getTimeInMillis()))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims;
        claims = extracAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractSpecialClaim(String jwt, String claim) {
        Claims claims;
        claims = extracAllClaims(jwt);
        return String.valueOf(claims.get(claim));
    }

    private Claims extracAllClaims(String token) {
        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        try {
            this.extractIdOrEmail(token);
            return false;
        } catch (Exception e) {
            return true;
        }
    }


    public String generateToken(User user) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, Object> claims = new HashMap<>();
        claims.put("schoolid", user.getLastSchool());
        user.getRoles().forEach(role -> role.getPermissions().forEach(perm -> stringBuilder.append(perm.getName()).append(",")));
        claims.put("perms", stringBuilder.toString());
        return createToken(claims, user);
    }

    public String createCookieToken(User user) {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.DATE, 30);
        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration.getTimeInMillis()))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    private String createToken(Map<String, Object> claims, User user) {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.MINUTE, 10);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(expiration.getTimeInMillis()))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    public Boolean validateToken(String token) {
        return (!isTokenExpired(token));
    }
}
