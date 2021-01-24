package multiplayer.server.controller;

import multiplayer.server.model.History;
import multiplayer.server.repository.HistoryRepository;
import multiplayer.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private UserService userService;

    public HistoryController(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @PostMapping
    public ResponseEntity addHistory(@RequestBody History history, @RequestHeader("secret") String secret) {

        boolean secretIsValid = secretIsValid(history, secret);

        System.out.println("Secret is valid: " + secretIsValid);

        if (secretIsValid) {

        boolean userIdValid =userService.userIdExists((long) history.getUserId());
        boolean opponentIdValid = userService.userIdExists((long) history.getOpponentId());

            if (userIdValid && opponentIdValid) {
                System.out.println("user ok");
                historyRepository.save(history);
                return new ResponseEntity(history, HttpStatus.OK);
            }
            else {
                System.out.println("at least one user is unknown");
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
    @Path("{userid}")
    public ResponseEntity getByUserId(@RequestParam("userid") Integer userId) {
        if (userService.userIdExists(Long.valueOf(userId))) {
            List<History> history = historyRepository.findByUserId(userId);
            if (history != null) {
                return new ResponseEntity(history, HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean secretIsValid (History history, String secret) {

        boolean userIdFound = secret.substring(1).indexOf(String.valueOf(history.getUserId())) == 0;
        boolean opponentIdFound = secret.substring(29).indexOf(String.valueOf(history.getOpponentId())) == 0;

        return userIdFound && opponentIdFound;
    }

}