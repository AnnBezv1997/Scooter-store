package com.ncedu.scooter.gateway.repository;

import com.ncedu.scooter.gateway.entity.Address;
import com.ncedu.scooter.gateway.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {
    ArrayList<Address> findAllByUser(User user);
}
