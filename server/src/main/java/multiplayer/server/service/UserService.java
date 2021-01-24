package multiplayer.server.service;


import multiplayer.server.model.ApplicationUser;
import multiplayer.server.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    public ApplicationUser getUserByUsername (@PathVariable String username) {
        return applicationUserRepository.findByUsername(username);
    }

    public boolean userIdExists (@PathVariable Long id) {

        Optional<ApplicationUser> user = applicationUserRepository.findById(id);
        return user.isPresent();
    }

    public ApplicationUser getUserByEmail (@PathVariable String email) {
        return applicationUserRepository.findByEmail(email);
    }

}