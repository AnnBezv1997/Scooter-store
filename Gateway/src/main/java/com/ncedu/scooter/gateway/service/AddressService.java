package com.ncedu.scooter.gateway.service;

import com.ncedu.scooter.gateway.entity.Address;
import com.ncedu.scooter.gateway.entity.User;
import com.ncedu.scooter.gateway.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public boolean saveUserAddress(Address address) {
        addressRepository.save(address);
        return true;

    }

    public ArrayList<Address> findAllUserAddress(User user) {
        return addressRepository.findAllByUser(user);
    }

    public boolean deleteAddress(Address address) {
        addressRepository.delete(address);
        return true;
    }
}
