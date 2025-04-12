package org.api.grocerystorebackend.controller;

import org.api.grocerystorebackend.dto.request.CartItemRequest;
import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.CartDTO;
import org.api.grocerystorebackend.dto.response.CartItemDTO;
import org.api.grocerystorebackend.entity.Cart;
import org.api.grocerystorebackend.entity.FlashSaleItem;
import org.api.grocerystorebackend.service.ICartService;
import org.api.grocerystorebackend.service.IFlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    ICartService cartService;

    @Autowired
    IFlashSaleService flashSaleService;
    @GetMapping()
    public ResponseEntity<ApiResponse<?>> getAllCarts(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size,
                                                      @RequestParam(name="userID") Long userID) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Page<CartDTO> listCarts = cartService.getCarts(pageable, userID);
            return ResponseEntity.ok(ApiResponse.ok("Lấy tất cả sản phẩm trong giỏ hàng thành công", listCarts));
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy tất cả sản phẩm trong giỏ hàng"));
        }
    }

    @PostMapping("/addToCart")
    public ResponseEntity<ApiResponse<?>> addToCart(@RequestBody CartItemRequest request) {
        try {
            cartService.addOrUpdateToCart(request.getCartItemDTO(), request.getUserID());
            return ResponseEntity.ok(ApiResponse.ok("Thêm sản phẩm vào giỏ hàng thành công", null));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi thêm sản phẩm vào giỏ hàng"));
        }
    }
}
