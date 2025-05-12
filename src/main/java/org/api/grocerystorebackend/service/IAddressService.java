package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.request.AddressRequest;
import org.api.grocerystorebackend.dto.response.AddressDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAddressService {
    Page<AddressDTO> getAllAddressesByUserId(Long userId, Pageable pageable);

    AddressDTO getAddressById(Long id);

    void createAddress(Long userId, AddressRequest addressRequest);

    boolean updateAddress(Long addressId, AddressRequest addressRequest);

    boolean deleteAddress(Long AddressId);

    boolean setDefaultAddress(Long AddressId);
}
