package com.tungstun.security.port.persistence.user;

import com.tungstun.security.domain.user.User;
import com.tungstun.security.domain.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DefaultUserRepository implements UserRepository {
    private final SpringUserRepository repository;

    public DefaultUserRepository(SpringUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User update(User entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Optional<User> findByMail(String mail) {
        return repository.findByMail(mail);
    }

    @Override
    public Optional<User> findByMailOrUsername(String mail, String username) {
        return repository.findByMailOrUsername(mail, username);
    }
}
