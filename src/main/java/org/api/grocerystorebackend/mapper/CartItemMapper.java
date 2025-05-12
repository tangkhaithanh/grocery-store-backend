package org.api.grocerystorebackend.mapper;

import org.api.grocerystorebackend.dto.response.CartItemDTO;
import org.api.grocerystorebackend.entity.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {
    @Autowired
    private ProductMapper productMapper;
    public CartItemDTO mapToDTO(CartItem cartItem) {
        return new CartItemDTO(
                cartItem.getId(),
                cartItem.getFlashSaleItem()==null ? null : cartItem.getFlashSaleItem().getId(),
                cartItem.getQuantity(),
                cartItem.getPrice(),
                productMapper.toDTO(cartItem.getProduct())
        );
    }
}
