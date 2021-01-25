package multiplayer.server.service;

import multiplayer.server.model.ApplicationUser;
import multiplayer.server.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * User Service class for checking and getting the users
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
@Service
public class UserService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    /**
     * Get a user by its user name
     *
     * @param username User name to get the user by
     *
     * @return ApplicationUser User that corresponds to the given user name
     */
    public ApplicationUser getUserByUsername (@PathVariable String username) {
        return applicationUserRepository.findByUsername(username);
    }

    /**
     * Checks if a user exists by its user id
     *
     * @param id User id to check the user by
     *
     * @return Boolean True or False depending if the user exists
     */
    public boolean userIdExists (@PathVariable Long id) {
        Optional<ApplicationUser> user = applicationUserRepository.findById(id);
        return user.isPresent();
    }

    /**
     * Get a user by its user email address
     *
     * @param email User email address to get the user by
     *
     * @return ApplicationUser User that corresponds to the given user email address
     */
    public ApplicationUser getUserByEmail (@PathVariable String email) {
        return applicationUserRepository.findByEmail(email);
    }

    /**
     * Get a user by its user id
     *
     * @param id User id to get the user by
     *
     * @return username that corresponds to the given user id
     */
    public String getUsernameById (@PathVariable Long id) {

        Optional<ApplicationUser> user = applicationUserRepository.findById(id);
        if (user.isPresent()) {
            return user.get().getUsername();
        }
        return "unknown";
    }

}