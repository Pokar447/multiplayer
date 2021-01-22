package multiplayer.server.controller;

import multiplayer.server.model.ApplicationUser;
import multiplayer.server.model.History;
import multiplayer.server.repository.ApplicationUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/users")
public class UserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(ApplicationUserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody ApplicationUser user, BindingResult bindingResult) {

        if (getUserByUsername(user.getUsername()) != null) {
            return new ResponseEntity("username already exists", HttpStatus.BAD_REQUEST);
        }
        if (getUserByEmail(user.getEmail()) != null) {
            return new ResponseEntity("email already exists", HttpStatus.BAD_REQUEST);
        }

        if (bindingResult.hasErrors()) {

            List<FieldError> errors = bindingResult.getFieldErrors();
            String errorMsg = "";
            for (FieldError e : errors){
                errorMsg += e.getField() + ":" + e.getDefaultMessage();
            }

            return new ResponseEntity(errorMsg, HttpStatus.BAD_REQUEST);
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    @Path("{username}")
    public ResponseEntity getIdByUsername(@PathParam("username") String username) {
        ApplicationUser user = getUserByUsername(username);
        if (user != null) {
            return new ResponseEntity(user.getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    public ApplicationUser getUserByUsername (@PathVariable String username) {
        return applicationUserRepository.findByUsername(username);
    }

    public ApplicationUser getUserByEmail (@PathVariable String email) {
        return applicationUserRepository.findByEmail(email);
    }
}