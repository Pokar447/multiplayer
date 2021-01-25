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

/**
 * History controller for receiving and responding REST API calls
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private UserService userService;

    /**
     * History controller constructor
     *
     * @param historyRepository Instance of the history repository
     */
    public HistoryController(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    /**
     * POST mapping to post history entries to the database
     *
     * @param history Instance of the history model
     * @param secret Secret to validate posts to the database
     *
     * @return ResponseEntity Response to a POST request
     */
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

    /**
     * GET mapping to get the history by a user id
     *
     * @param userId User id to get the history for
     *
     * @return ResponseEntity Response to a GET request
     */
    @GetMapping
    @Path("{userid}")
    public ResponseEntity getByUserId(@RequestParam("userid") Integer userId) {
        if (userService.userIdExists(Long.valueOf(userId))) {
            List<History> historyList = historyRepository.findByUserId(userId);
            for (History currHistory : historyList) {
                currHistory.setOpponentname(userService.getUsernameById( (long) currHistory.getOpponentId()));
            }
            if (historyList != null) {
                return new ResponseEntity(historyList, HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Checks if the POST request is valid by a secret
     *
     * @param history Instance of the history model
     * @param secret Secret to validate posts to the database
     *
     * @return Boolean Returns true if the user id and the opponent id is valid
     */
    private boolean secretIsValid (History history, String secret) {

        boolean userIdFound = secret.substring(1).indexOf(String.valueOf(history.getUserId())) == 0;
        boolean opponentIdFound = secret.substring(29).indexOf(String.valueOf(history.getOpponentId())) == 0;

        return userIdFound && opponentIdFound;
    }

}