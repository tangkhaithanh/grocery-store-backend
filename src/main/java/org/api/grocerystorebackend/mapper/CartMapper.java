package org.api.grocerystorebackend.mapper;

import org.api.grocerystorebackend.dto.response.CartDTO;
import org.api.grocerystorebackend.dto.response.CartItemDTO;
import org.api.grocerystorebackend.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {
    @Autowired
    private CartItemMapper cartItemMapper;
    public CartDTO mapToDTO(Cart cart) {
        List<CartItemDTO> listCartItems = cart.getCartItems().stream()
                .map(cartItemMapper::mapToDTO)
                .toList();

        return new CartDTO(
                cart.getId(),
                listCartItems
        );

    }
}
