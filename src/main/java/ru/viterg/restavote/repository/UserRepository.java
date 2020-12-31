package ru.viterg.restavote.repository;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import ru.viterg.restavote.AuthorizedUser;
import ru.viterg.restavote.model.User;

import java.util.List;

@Repository("userService")
public class UserRepository implements UserDetailsService {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository repository;

    public UserRepository(CrudUserRepository repository) {
        this.repository = repository;
    }

    public User save(User user) {
        return repository.save(user);
    }

    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }

    public User get(int id) {
        return repository.findById(id).orElse(null);
    }

    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    public List<User> getAll() {
        return repository.findAll(SORT_NAME_EMAIL);
    }

    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}