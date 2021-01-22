package multiplayer.server.controller;

import multiplayer.server.model.History;
import multiplayer.server.repository.HistoryRepository;
import org.springframework.web.bind.annotation.*;

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
    public List<History> getHistories() {
        return historyRepository.findAll();
    }

}