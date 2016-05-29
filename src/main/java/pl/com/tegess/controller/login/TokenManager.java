package pl.com.tegess.controller.login;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.codec.Base64;
import pl.com.tegess.domain.application.Application;
import pl.com.tegess.domain.user.User;

import java.util.HashMap;

public class TokenManager {

    public String generateJWTTokenForUser(User user, Application application) {

        HashMap<String, Object> claims = new HashMap<>();

        claims.put("username", user.getUsername());
        claims.put("privileges", user.getPrivileges());

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(user.getUsername())
                .setClaims(claims)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.HS256, Base64.encode(application.getSecret().getBytes()));

        return jwtBuilder.compact();
    }
}
