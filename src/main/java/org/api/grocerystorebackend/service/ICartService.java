package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.request.CartItemRequest;
import org.api.grocerystorebackend.dto.response.CartDTO;
import org.api.grocerystorebackend.dto.response.CartItemDTO;
import org.api.grocerystorebackend.dto.response.ProductSimpleDTO;
import org.api.grocerystorebackend.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICartService {
    CartDTO getCarts(Long userId);
    void addToCart(CartItemRequest cartItem, Long userId);

    void removeToCart(Long cartItemId, Long userId);
    void updateToCart(CartItemRequest cartItem, Long userId);
}
