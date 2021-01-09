package multiplayer.server.controller;

import multiplayer.server.model.History;
import multiplayer.server.repository.HistoryRepository;
import org.springframework.util.Assert;
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

    @PutMapping("/{id}")
    public void editHistory(@PathVariable long id, @RequestBody History history) {
        History existingHistory = historyRepository.findById(id).get();
        Assert.notNull(existingHistory, "History not found");
        existingHistory.setDescription(history.getDescription());
        historyRepository.save(existingHistory);
    }

    @DeleteMapping("/{id}")
    public void deleteHistory(@PathVariable long id) {
        History historyToDel = historyRepository.findById(id).get();
        historyRepository.delete(historyToDel);
    }
}