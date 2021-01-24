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
    public ResponseEntity addHistory(@RequestBody History history) {

        boolean userIdValid =userService.userIdExists((long) history.getUserId());
        boolean opponentIdValid = userService.userIdExists((long) history.getOpponentId());

        if (userIdValid && opponentIdValid) {
            historyRepository.save(history);
            return new ResponseEntity(history, HttpStatus.OK);
        }
        else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    @Path("{userid}")
    public ResponseEntity getByUserId(@RequestParam("userid") Integer userId) {
        System.out.println("TEST JORN: " + userId);

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

}