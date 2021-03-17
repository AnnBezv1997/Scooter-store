package com.ncedu.scooter.gateway.repository;

import com.ncedu.scooter.gateway.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(String name);
}
