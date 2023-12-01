package store.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import store.customer.CustomerController;
import store.customer.CustomerOut;
import store.customer.CustomertIn;

@Service
public class AuthService {

    @Autowired
    private CustomerController customerController;

    @Autowired
    private JwtService jwtService;

    public String login(String email, String password) {
        CustomerOut found = customerController.login(new CustomertIn(null, null, null, email, password));
        if (found == null) throw new RuntimeException("credentials do not match");
        return jwtService.create(found.id(), found.name());
    }

    public String id(String token) {
        return jwtService.getId(token);
    }

}
