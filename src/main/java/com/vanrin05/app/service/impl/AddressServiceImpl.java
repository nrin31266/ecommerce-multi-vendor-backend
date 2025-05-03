package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.request.AddressRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.mapper.AddressMapper;
import com.vanrin05.app.model.Address;
import com.vanrin05.app.model.User;
import com.vanrin05.app.repository.AddressRepository;
import com.vanrin05.app.repository.UserRepository;
import com.vanrin05.app.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserRepository userRepository;
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

    @Override
    public Address createUserAddress(AddressRequest request, User user) {
        Address address = addressMapper.toAddress(request);
        address.setUser(user);
        List<Address> currentDefaults = addressRepository.findAllByUserAndIsDefaultTrue(user);
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            currentDefaults.forEach(it -> it.setIsDefault(false));
            addressRepository.saveAll(currentDefaults);
        }else{
            if(currentDefaults.isEmpty()){
                address.setIsDefault(true);
            }
        }

        return addressRepository.save(address);
    }


    @Override
    public Address defaultUserAddress(User user) {
        List<Address> addresses = addressRepository.findAllByUserAndIsDefaultTrue(user);
        if(!addresses.isEmpty()){
            return addresses.getFirst();
        }
        return null;
    }

    @Override
    public List<Address> getAllAddressesByUser(User user) {

        return addressRepository.findAllByUser(user);
    }


}
