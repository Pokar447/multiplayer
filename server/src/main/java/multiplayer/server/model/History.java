package multiplayer.server.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotNull
    @Column(length = 20, nullable = false)
    int userId;

    @NotNull
    @Column(length = 20, nullable = false)
    int opponentId;

    @NotNull
    @Column(length = 20, nullable = false)
    int userHp;

    @NotNull
    @Column(length = 20, nullable = false)
    int opponentHp;

    @CreationTimestamp
    @Column
    LocalDateTime dateTime = LocalDateTime.now();

}