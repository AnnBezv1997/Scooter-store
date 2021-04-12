package com.ncedu.scooter.gateway.repository;

import com.ncedu.scooter.gateway.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByLogin(String login);


}
