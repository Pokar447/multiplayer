package multiplayer.server.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * Represents a user in the application
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
@Getter
@Setter
@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Length(min = 3, max = 15, message = "Username length must be between 3 and 15!") // -> zur Validierung gedacht!!!
    @NotEmpty(message = "Username cannot not be empty!")
    @Column(length = 100, nullable = false, unique = true) // -> beschreibt wie der Attribut in DB angelegt wird. Nicht zur Validierung gedacht!!!
    private String username;

    @NotEmpty(message = "Email cannot not be empty!")
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "Password cannot not be empty!")
    @Column(length = 100, nullable = false)
    private String password;

}