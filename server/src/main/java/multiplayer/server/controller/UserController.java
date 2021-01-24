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
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(ApplicationUserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

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

    @GetMapping("/image")
    public ResponseEntity getImage() throws IOException {
        File img = new ClassPathResource("images/profile_picture.png").getFile();
        byte[] fileContent = Files.readAllBytes(img.toPath());
        return new ResponseEntity(fileContent, HttpStatus.OK);
    }

//    @RequestMapping(value = "/image-manual-response", method = RequestMethod.GET)
//    public void getImageAsByteArray(HttpServletResponse response) throws IOException {
//        Image in = new Image(getClass().getResource("/images/profile_picture.png").toString());
//        response.setContentType(MediaType.IMAGE_PNG_VALUE);
//        IOUtils.copy(in, response.getOutputStream());
//    }

//    @GetMapping(
//            value = "/get-file",
//            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
//    )
//    public @ResponseBody byte[] getFile() throws IOException {
//        InputStream in = getClass()
//                .getResourceAsStream("images/profile_picture.png");
//        return IOUtils.toByteArray(in);
//    }

}