package org.api.grocerystorebackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.api.grocerystorebackend.dto.request.CartItemRequest;
import org.api.grocerystorebackend.dto.response.CartDTO;
import org.api.grocerystorebackend.dto.response.CartItemDTO;
import org.api.grocerystorebackend.dto.response.ProductSimpleDTO;
import org.api.grocerystorebackend.entity.Cart;
import org.api.grocerystorebackend.entity.CartItem;
import org.api.grocerystorebackend.entity.FlashSaleItem;
import org.api.grocerystorebackend.entity.Product;
import org.api.grocerystorebackend.mapper.CartMapper;
import org.api.grocerystorebackend.repository.CartItemRepository;
import org.api.grocerystorebackend.repository.CartRepository;
import org.api.grocerystorebackend.repository.ProductRepository;
import org.api.grocerystorebackend.repository.UserRepository;
import org.api.grocerystorebackend.service.ICartService;
import org.api.grocerystorebackend.service.IFlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    IFlashSaleService flashSaleService;

    @Autowired
    CartMapper cartMapper;

    @Override
    public CartDTO getCarts(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if(cart != null) {
            return cartMapper.mapToDTO(cart);
        }
        return null;
    }

    @Transactional
    @Override
    public void addOrUpdateToCart(CartItemRequest cartItem, Long userId) {
        boolean isOutOfStock = false; //dùng để check số lượng muốn mua > số lượng tồn kho của FL
        //1. Kiểm tra user đã có giỏ hàng hay chưa? Tạo hoặc cập nhật nếu đã có
        Cart cart = cartRepository.findById(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userRepository.findById(userId).get());
                    newCart.setCreatedAt(LocalDateTime.now());
                    return cartRepository.save(newCart);
                });

        //2. Kiểm tra sản phẩm có nằm trong chương trình FL? Nếu có lấy min(maxPerCustomer,n) với giá FL, còn lại thì lấy với giá gốc.
        Optional<FlashSaleItem> productInFlashSale = flashSaleService.findProductInFlashSale(cartItem.getProduct().getId());

        if(productInFlashSale.isPresent()) {
            int quantityAcceptBuy = Math.min(productInFlashSale.get().getMaxPerCustomer(), cartItem.getQuantity());
            CartItem cartItemHasFL = new CartItem();
            cartItemHasFL.setCart(cart);
            cartItemHasFL.setProduct(productInFlashSale.get().getProduct());
            cartItemHasFL.setFlashSaleItem(productInFlashSale.get());
            //2.1. Số lượng muốn mua >  Số lượng tồn kho trong FL
            if(quantityAcceptBuy + productInFlashSale.get().getSoldQuantity() > productInFlashSale.get().getStockQuantity()) {
                cartItemHasFL.setQuantity(productInFlashSale.get().getStockQuantity()-productInFlashSale.get().getSoldQuantity());
                isOutOfStock = true;
            }
            else {
                cartItemHasFL.setQuantity(quantityAcceptBuy);
            }
            cartItemHasFL.setPrice(productInFlashSale.get().getFlashSalePrice());
            //2.2. Kiểm tra sản phẩm có nằm trong cart_item?
            Optional<CartItem> productInCartItem = cartItemRepository.findByProductIdWithFL(cartItem.getProduct().getId(), cart.getId());
            if(productInCartItem.isPresent()) {
                productInCartItem.get().setQuantity(productInCartItem.get().getQuantity()+cartItemHasFL.getQuantity());
                cartItemRepository.save(productInCartItem.get());
            }
            else {
                cartItemRepository.save(cartItemHasFL);
            }
        }
        else {
            //2.1. Kiểm tra sản phẩm ko thuộc FL đã có trong giỏ hàng?
            Optional<CartItem> oldCartItem = cartItemRepository.findByProductIdWithNotFL(cartItem.getProduct().getId(), cart.getId());
            if(oldCartItem.isPresent()) {
                oldCartItem.get().setQuantity(oldCartItem.get().getQuantity() + cartItem.getQuantity());
                cartItemRepository.save(oldCartItem.get());
            }
            else {
                CartItem newCartItem = new CartItem();
                newCartItem.setCart(cart);
                newCartItem.setProduct(productRepository.findById(cartItem.getProduct().getId()).get());
                newCartItem.setQuantity(cartItem.getQuantity());
                newCartItem.setPrice(cartItem.getPrice());
                cartItemRepository.save(newCartItem);
            }
        }
        //Thiếu check isOutOfStock -> Chưa nghĩ ra.
    }

    @Override
    public void removeToCart(Long cartItemId, Long userId) {
        Optional<CartItem> cartItem = Optional.ofNullable(cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found")));
        if(!cartItem.get().getCart().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Bạn không có quyền xóa sản phẩm này khỏi giỏ hàng");
        }
        cartItemRepository.deleteById(cartItemId);
    }
}
