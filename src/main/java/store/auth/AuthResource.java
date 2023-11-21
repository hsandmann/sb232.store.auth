package store.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*")
@RestController()
public class AuthResource implements AuthController {

    @Autowired
    private AuthService authService;

    @Override
    public LoginOut login(LoginIn login) {
        if (login.email() == null || login.email().trim().length() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email is missing");
        if (login.password() == null || login.password().trim().length() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password is missing");
        try {
            return new LoginOut(
                authService.login(login.email().trim(), login.password().trim())
            );
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
