package de.structuremade.ms.authservice.api.service;

import de.structuremade.ms.authservice.database.entity.School;
import de.structuremade.ms.authservice.database.entity.User;
import de.structuremade.ms.authservice.database.repo.SchoolRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    SchoolRepository schoolRepository;

    /*@Bean
    private void blacklistedTokenSort() {
        if (!blacklistMap.isEmpty()) {
            for (int i = 0; i <= blacklistMap.size(); i++) {
                if (extractExpiration(blacklistMap.get(i)).before(new Date())) {
                    blacklistMap.remove(blacklistMap.get(i));
                }
            }
        }
    }*/

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
        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000L))
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


    @Transactional
    public String generateToken(User user) {
        School school = schoolRepository.getOne(user.getLastSchool());
        StringBuilder permissions= new StringBuilder();
        StringBuilder childrens= new StringBuilder();
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstname", user.getFirstname());
        claims.put("name", user.getName());
        claims.put("schoolid", user.getLastSchool());
        user.getChildrens().forEach(children -> childrens.append(children.getId()).append(","));
        claims.put("children",childrens.toString());
        claims.put("class", user.getUserClass().getId());
        user.getRoles().forEach(role -> {
            if (role.getSchool() == school){
                role.getPermissions().forEach(perm -> permissions.append(perm.getName()).append(","));
            }
        });
        user.getPermissions().forEach(perm -> permissions.append(perm.getPermission().getName()).append(","));
        claims.put("perms", permissions.toString());
        return createToken(claims, user);
    }

    public String createCookieToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2592000000L))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    private String createToken(Map<String, Object> claims, User user) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000L))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    public Boolean validateToken(String token) {
        return (!isTokenExpired(token));
    }
}
