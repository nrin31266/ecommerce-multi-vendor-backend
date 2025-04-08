package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.request.AddressRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.mapper.AddressMapper;
import com.vanrin05.app.model.Address;
import com.vanrin05.app.repository.AddressRepository;
import com.vanrin05.app.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(()->new AppException("Address not found"));
    }

    @Override
    public Address createAddress(AddressRequest request) {
        Address address = addressMapper.toAddress(request);
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(AddressRequest request, Long id) {
        Address address = getAddressById(id);
        addressMapper.updateAddress(address, request);
        return address;
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = getAddressById(id);
        addressRepository.delete(address);
    }
}
