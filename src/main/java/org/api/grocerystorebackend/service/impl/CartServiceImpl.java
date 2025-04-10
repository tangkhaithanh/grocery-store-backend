package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.dto.response.CartDTO;
import org.api.grocerystorebackend.entity.Cart;
import org.api.grocerystorebackend.mapper.CartMapper;
import org.api.grocerystorebackend.repository.CartRepository;
import org.api.grocerystorebackend.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartMapper cartMapper;

    @Override
    public Page<CartDTO> getCarts(Pageable pageable, Long userId) {
        Page<Cart> listCarts = cartRepository.findAllByUserId(pageable, userId);
        return listCarts.map(cartMapper::mapToDTO);
    }
}
