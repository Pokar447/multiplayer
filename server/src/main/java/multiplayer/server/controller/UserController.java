package multiplayer.server.controller;

import multiplayer.server.model.ApplicationUser;
import multiplayer.server.repository.ApplicationUserRepository;
import multiplayer.server.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Application user controller for receiving and responding REST API calls
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Application user controller constructor
     *
     * @param applicationUserRepository Instance of the application user repository
     * @param bCryptPasswordEncoder Instance of the BCryptPasswordEncoder
     */
    public UserController(ApplicationUserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * POST mapping to register a new user and store in the database
     *
     * @param user Instance of the application user model
     * @param bindingResult Result of the application user binding
     *
     * @return ResponseEntity Response to a POST request
     */
    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody ApplicationUser user, BindingResult bindingResult) {

        if (userService.getUserByUsername(user.getUsername()) != null) {
            return new ResponseEntity("username already exists", HttpStatus.BAD_REQUEST);
        }
        if (userService.getUserByEmail(user.getEmail()) != null) {
            return new ResponseEntity("email already exists", HttpStatus.BAD_REQUEST);
        }

        if (bindingResult.hasErrors()) {

            List<FieldError> errors = bindingResult.getFieldErrors();
            String errorMsg = "";
            for (FieldError e : errors){
                errorMsg += e.getDefaultMessage();
            }

            return new ResponseEntity(errorMsg, HttpStatus.BAD_REQUEST);
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        applicationUserRepository.save(user);

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * GET mapping to get the user id by a user name
     *
     * @param username User name to get the user id for
     *
     * @return ResponseEntity Response to a GET request
     */
    @GetMapping
    @Path("{username}")
    public ResponseEntity getIdByUsername(@PathParam("username") String username) {
        ApplicationUser user = userService.getUserByUsername(username);
        if (user != null) {
            return new ResponseEntity(user.getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET mapping to get the default profile picture from the server
     *
     * @return ResponseEntity Response to a GET request
     */
    @GetMapping("/image")
    public ResponseEntity getImage() throws IOException {
        File img = new ClassPathResource("images/profile_picture.png").getFile();
        byte[] fileContent = Files.readAllBytes(img.toPath());
        return new ResponseEntity(fileContent, HttpStatus.OK);
    }

}