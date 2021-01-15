package multiplayer.server.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Length(min = 3, max = 15) // -> zur Validierung gedacht!!!
    @NotEmpty
    @Column(length = 100, nullable = false, unique = true) // -> beschreibt wie der Attribut in DB angelegt wird. Nicht zur Validierung gedacht!!!
    String username;

    @NotEmpty
    @Column(length = 100, nullable = false, unique = true)
    String email;

    @NotEmpty
    @Column(length = 100, nullable = false)
    String password;

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}