package com.vanrin05.app.service;


import com.vanrin05.app.dto.request.AddressRequest;
import com.vanrin05.app.model.Address;

public interface AddressService {
    Address getAddressById(Long id);
    Address createAddress(AddressRequest request);
    Address updateAddress(AddressRequest request, Long id);
    void deleteAddress(Long id);
}
