package pl.com.tegess.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;
import pl.com.tegess.domain.admin.Admin;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class TokenHandler {

    private final String secret;
    private final AdminService adminService;

    public TokenHandler(String secret, AdminService adminService) {
        this.secret = secret;
        this.adminService = adminService;
    }

    public Admin parseUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        UserDetails userDetails = adminService.loadUserByUsername(claims.get("username", String.class));
        Admin admin = new Admin(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
        return admin;
    }

    public String createTokenForAdmin(String username, Collection<? extends GrantedAuthority> authorities) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, Base64.encode(secret.getBytes()))
                .setHeaderParam("typ", "JWT");

        List<String> authoritiesAsString  = authorities.stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("username", username);
        hashMap.put("authorities", authoritiesAsString);

        jwtBuilder.setClaims(hashMap);

        return jwtBuilder.compact();
    }
}
