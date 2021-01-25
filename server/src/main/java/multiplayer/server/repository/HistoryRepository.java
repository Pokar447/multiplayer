package multiplayer.server.repository;

import multiplayer.server.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * HistoryRepository interface for finding histories by id
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByUserId(Integer userId);
}