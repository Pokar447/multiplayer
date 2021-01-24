package multiplayer.server.model;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Length(min = 3, max = 15, message = "username length must be between 3 and 15") // -> zur Validierung gedacht!!!
    @NotEmpty(message = "username cannot not be empty")
    @Column(length = 100, nullable = false, unique = true) // -> beschreibt wie der Attribut in DB angelegt wird. Nicht zur Validierung gedacht!!!
    String username;

    @NotEmpty(message = "email cannot not be empty")
    @Column(length = 100, nullable = false, unique = true)
    String email;

    @NotEmpty(message = "password cannot not be empty")
    @Column(length = 100, nullable = false)
    String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}