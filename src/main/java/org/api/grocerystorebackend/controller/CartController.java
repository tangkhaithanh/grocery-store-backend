package org.api.grocerystorebackend.controller;

import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.CartDTO;
import org.api.grocerystorebackend.dto.response.CartItemDTO;
import org.api.grocerystorebackend.entity.Cart;
import org.api.grocerystorebackend.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    ICartService cartService;
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
    public ResponseEntity<ApiResponse<?>> addToCart(@RequestBody CartItemDTO cartItemDTO) {
        try {

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
