package com.vanrin05.app.repository;

import com.vanrin05.app.model.Address;
import com.vanrin05.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUserAndIsDefaultTrue(User currentUser);
    List<Address> findAllByUser(User currentUser);
}
