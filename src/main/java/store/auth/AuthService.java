package store.auth;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import store.customer.CustomerController;
import store.customer.CustomerOut;
import store.customer.CustomertIn;

@Service
public class AuthService {

    @Autowired
    private CustomerController customerController;

    public String login(String email, String password) {
        CustomerOut found = customerController.login(new CustomertIn(null, null, null, email, password));
        if (found == null) throw new RuntimeException("credentials do not match");
        return createJWT(found.id());
    }

    private String createJWT(String id) {
        SecretKey key = Jwts.SIG.HS256.key().build();
        String jwt = Jwts.builder()
            .header()
            .keyId("EsPm.Store")
            .and()
            .subject(id)
            .signWith(key)
            .compact();
        return jwt;
    }

}
