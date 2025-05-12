package org.api.grocerystorebackend.service.impl;

import jakarta.transaction.Transactional;
import org.api.grocerystorebackend.dto.request.AddressRequest;
import org.api.grocerystorebackend.dto.response.AddressDTO;
import org.api.grocerystorebackend.entity.Address;
import org.api.grocerystorebackend.entity.User;
import org.api.grocerystorebackend.mapper.AddressMapper;
import org.api.grocerystorebackend.repository.AddressRepository;
import org.api.grocerystorebackend.repository.UserRepository;
import org.api.grocerystorebackend.service.IAddressService;
import org.api.grocerystorebackend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<AddressDTO> getAllAddressesByUserId(Long userId, Pageable pageable) {
        Page<Address> addressPage=addressRepository.findByUserId(userId, pageable);
        return addressPage.map(addressMapper::toDTO);
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        Optional<Address> addressOptional = addressRepository.findById(id);
        return addressOptional.map(addressMapper::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public void createAddress(Long userId, AddressRequest addressRequest) {
        User user = userRepository.findById(userId).orElse(null);
        List<Address> existingAddresses = addressRepository.findByUserId(userId);
        boolean  shouldBeDefault = existingAddresses.isEmpty();
        Address address = new Address();
        address.setUser(user);
        address.setCity(addressRequest.getCity());
        address.setDistrict(addressRequest.getDistrict());
        address.setStreetAddress(addressRequest.getStreetAddress());
        address.setUserName(addressRequest.getUserName());
        address.setPhoneNumber(addressRequest.getPhoneNumber());
        address.setDefault(shouldBeDefault);
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());
        addressRepository.save(address);
    }

    @Override
    @Transactional
    public boolean updateAddress(Long addressId, AddressRequest addressRequest) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);

        Address address = optionalAddress.get();
        address.setCity(addressRequest.getCity());
        address.setDistrict(addressRequest.getDistrict());
        address.setStreetAddress(addressRequest.getStreetAddress());
        address.setUserName(addressRequest.getUserName());
        address.setPhoneNumber(addressRequest.getPhoneNumber());
        address.setUpdatedAt(LocalDateTime.now());
        addressRepository.save(address);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteAddress(Long AddressId) {
        Optional<Address> addressOptional = addressRepository.findById(AddressId);
        if (addressOptional.isEmpty()) {
            return false;
        }

        Address address = addressOptional.get();
        Long userId = address.getUser().getId();
        List<Address> userAddresses = addressRepository.findByUserId(userId);
        if (userAddresses.size() <= 1) {
            return false;
        }
        if (address.isDefault()) {
            return false;
        }

        // Thực hiện xóa địa chỉ
        addressRepository.deleteById(AddressId);
        return true;
    }

    @Override
    @Transactional
    public boolean setDefaultAddress(Long AddressId) {
        Optional<Address> addressOptional = addressRepository.findById(AddressId);
        if (addressOptional.isEmpty()) {
            return false;
        }

        Address address = addressOptional.get();
        Long userId = address.getUser().getId();

        // Nếu địa chỉ đã là mặc định, không cần thay đổi gì
        if (address.isDefault()) {
            return true;
        }

        // Đặt tất cả các địa chỉ khác của người dùng này thành không phải mặc định
        List<Address> defaultAddresses = addressRepository.findByUserIdAndIsDefaultTrue(userId);
        defaultAddresses.forEach(addr -> {
            addr.setDefault(false);
            addr.setUpdatedAt(LocalDateTime.now());
            addressRepository.save(addr);
        });

        // Đặt địa chỉ này làm mặc định
        address.setDefault(true);
        address.setUpdatedAt(LocalDateTime.now());
        addressRepository.save(address);
        return true;
    }
}
