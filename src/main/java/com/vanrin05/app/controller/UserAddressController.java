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

import java.util.List;

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

        List<Address> addresses = user.getAddresses();
        if (addresses.size() > 5) {
            throw new AppException("You can have at most 5 addresses");
        }

        return ResponseEntity.ok(addressService.createUserAddress(request, user));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long addressId, @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(request, addressId));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

}
