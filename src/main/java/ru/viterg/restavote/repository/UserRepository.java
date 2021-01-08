package ru.viterg.restavote.repository;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.viterg.restavote.AuthorizedUser;
import ru.viterg.restavote.model.User;
import ru.viterg.restavote.util.UserUtil;

import java.util.List;

@Repository
// @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserRepository implements UserDetailsService {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository repository;
    private final PasswordEncoder    passwordEncoder;

    public UserRepository(CrudUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        return repository.save(UserUtil.prepareToSave(user, passwordEncoder));
    }

    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }

    public User get(int id) {
        return repository.findById(id).orElse(null);  //orElseThrow
    }

    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    public List<User> getAll() {
        return repository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}