package ru.viterg.restavote.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.viterg.restavote.*;
import ru.viterg.restavote.model.User;
import ru.viterg.restavote.repository.UserRepository;
import ru.viterg.restavote.to.UserTo;
import ru.viterg.restavote.util.UserUtil;

import java.util.List;

import static ru.viterg.restavote.util.UserUtil.prepareToSave;
import static ru.viterg.restavote.util.ValidationUtil.*;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserRepository repository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public List<User> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public User create(UserTo userTo) {
        log.info("create from to {}", userTo);
        return create(UserUtil.createNewFromTo(userTo));
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        log.info("delete {}", id);
        // checkModificationAllowed(id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void update(UserTo userTo, int id) {
        log.info("update {} with id={}", userTo, id);
        // checkModificationAllowed(userTo.id());
        User user = repository.get(userTo.id());
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    @CacheEvict(value = "users", allEntries = true)
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        // checkModificationAllowed(user.id());
        prepareAndSave(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(int id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        // checkModificationAllowed(id);
        User user = get(id);
        user.setEnabled(enabled);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    private User prepareAndSave(User user) {
        return repository.save(prepareToSave(user, passwordEncoder));
    }

    protected void validateBeforeUpdate(HasId user, int id) throws BindException {
        assureIdConsistent(user, id);
        DataBinder binder = new DataBinder(user);
        binder.addValidators(emailValidator, validator);
        binder.validate(View.Web.class);
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }
}