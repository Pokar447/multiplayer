package multiplayer.server.controller;

import multiplayer.server.model.History;
import multiplayer.server.repository.HistoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private HistoryRepository historyRepository;

    public HistoryController(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @PostMapping
    public void addHistory(@RequestBody History history) {
        historyRepository.save(history);
    }

    @GetMapping
    @Path("{userid}")
    public ResponseEntity getByUserId(@RequestParam("userid") Integer userId) {
        List<History> history = historyRepository.findByUserId(userId);
        if (history != null) {
            return new ResponseEntity(history, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}