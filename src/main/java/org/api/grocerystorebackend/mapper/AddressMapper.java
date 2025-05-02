package org.api.grocerystorebackend.mapper;

import org.api.grocerystorebackend.dto.response.AddressDTO;
import org.api.grocerystorebackend.entity.Address;
import org.hibernate.annotations.Cache;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public AddressDTO toDTO(Address address) {
        return new AddressDTO(
                address.getId(),
                address.getUser().getId(),
                address.getCity(),
                address.getDistrict(),
                address.getStreetAddress(),
                address.getUserName(),
                address.getPhoneNumber(),
                address.isDefault(),
                address.getCreatedAt(),
                address.getUpdatedAt()
        );
    }

    public Page<AddressDTO> toDTOPage(Page<Address> addressPage) {
        return addressPage.map(this::toDTO);
    }
}
