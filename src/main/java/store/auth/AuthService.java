package store.auth;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import store.customer.CustomerController;
import store.customer.CustomerOut;
import store.customer.CustomertIn;

@Service
public class AuthService {

    @Value("${store.jwt.secretKey}")
    private String secretKey;

    @Value("${store.jwt.issuer}")
    private String issuer;

    @Value("${store.jwt.duration}")
    private long duration;


    @Autowired
    private CustomerController customerController;

    public String login(String email, String password) {
        CustomerOut found = customerController.login(new CustomertIn(null, null, null, email, password));
        if (found == null) throw new RuntimeException("credentials do not match");
        return createJWT(found.id());
    }

    private String createJWT(String id) {        
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        String jwt = Jwts.builder()
            .header()
            .keyId("EsPm.Store")
            .and()
            .issuer(issuer)
            .subject(id)
            .signWith(key)
            .notBefore(new Date())
            .expiration(new Date(new Date().getTime() + duration))
            .compact();
        return jwt;
    }

}
