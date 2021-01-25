package multiplayer.server.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Represents a history for a user
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
@Getter
@Setter
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(length = 20, nullable = false)
    private int userId;

    @NotNull
    @Column(length = 20, nullable = false)
    private int opponentId;

    @NotNull
    @Column(length = 20, nullable = false)
    private int userHp;

    @NotNull
    @Column(length = 20, nullable = false)
    private int opponentHp;

    @CreationTimestamp
    @Column
    private LocalDateTime dateTime = LocalDateTime.now();

    @Transient
    String opponentname;
}