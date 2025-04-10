package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.response.CartDTO;
import org.api.grocerystorebackend.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICartService {
    Page<CartDTO> getCarts(Pageable pageable, Long userId);
}
