package com.vanrin05.app.controller;

import com.vanrin05.app.dto.request.AddressRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.Address;
import com.vanrin05.app.model.User;
import com.vanrin05.app.service.AddressService;
import com.vanrin05.app.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users/address")
@RequiredArgsConstructor
public class UserAddressController {
    private final AddressService addressService;
    private final UserService userService;

    @GetMapping("/{addressId}")
    public ResponseEntity<Address> getAddress(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.getAddressById(addressId));
    }

    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody AddressRequest request, @RequestHeader("Authorization") String token) {

        User user = userService.findUserByJwtToken(token);

//        Set<Address> addressesCopy = new HashSet<>(user.getAddresses());
//        if (addressesCopy.size() > 5) {
//            throw new AppException("You can have at most 5 addresses");
//        }


        return ResponseEntity.ok(addressService.createUserAddress(request, user));
    }

    @GetMapping("/default")
    public ResponseEntity<Address> defaultAddress(@RequestHeader("Authorization") String token) {

        User user = userService.findUserByJwtToken(token);


        return ResponseEntity.ok(addressService.defaultUserAddress( user));
    }
    @GetMapping
    public ResponseEntity<List<Address>> getAllUserAddresses(@RequestHeader("Authorization") String token) {

        User user = userService.findUserByJwtToken(token);

        return ResponseEntity.ok(addressService.getAllAddressesByUser(user));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long addressId, @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(request, addressId));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId, @RequestHeader("Authorization") String token) {
        User user = userService.findUserByJwtToken(token);
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

}
