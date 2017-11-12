package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {

    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);

    {
        save(new User("admin", "admin@mail.ru", "admin", Role.ROLE_ADMIN));
        save(new User("user", "user@mail.ru", "user", Role.ROLE_USER));
    }

    @Override
    public boolean delete(int id) {
        User user = repository.remove(id);
        if (user == null) return false;
        log.info("delete user {}", user.toString());
        return true;
    }

    @Override
    public User save(User user) {
        if (user.isNew()) user.setId(counter.incrementAndGet());
        repository.put(user.getId(), user);
        log.info("save user {}", user.toString());
        return user;
    }

    @Override
    public User get(int id) {
        User user = repository.get(id);
        if (user != null) log.info("get user %s", user.toString());
        else log.info("user id {} is not found", id);
        return user;
    }

    @Override
    public List<User> getAll() {
        log.info("get all users");
        return new ArrayList<>(repository.values());
    }

    @Override
    public User getByEmail(String email) {
        User user = repository.values()
                .parallelStream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
        if (user != null) log.info("get user by email {}", email);
        else log.info("email {} is not found" + email);
        return user;
    }
}
