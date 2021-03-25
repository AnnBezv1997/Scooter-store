package com.ncedu.scooter.gateway.repository;

import com.ncedu.scooter.gateway.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByLogin(String login);
}
