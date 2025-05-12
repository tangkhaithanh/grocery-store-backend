	package org.api.grocerystorebackend.controller;

import jakarta.persistence.EntityNotFoundException;
import org.api.grocerystorebackend.dto.request.CartItemRequest;
import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.CartDTO;
import org.api.grocerystorebackend.dto.response.CartItemDTO;
import org.api.grocerystorebackend.entity.Cart;
import org.api.grocerystorebackend.entity.FlashSaleItem;
import org.api.grocerystorebackend.security.AccountDetails;
import org.api.grocerystorebackend.service.ICartService;
import org.api.grocerystorebackend.service.IFlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
                                                      @AuthenticationPrincipal AccountDetails accountDetails) {
        try {
            Long userId = accountDetails.getAccount().getUser().getId();
            CartDTO cart = cartService.getCarts(userId);
            return ResponseEntity.ok(ApiResponse.ok("Lấy tất cả sản phẩm trong giỏ hàng thành công", cart));
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy tất cả sản phẩm trong giỏ hàng"));
        }
    }

    @PostMapping("/addToCart")
    public ResponseEntity<ApiResponse<?>> addToCart(@RequestBody CartItemRequest request, @AuthenticationPrincipal AccountDetails accountDetails) {
        try {
            Long userId = accountDetails.getAccount().getUser().getId();

            cartService.addOrUpdateToCart(request, userId);
            return ResponseEntity.ok(ApiResponse.ok("Thêm sản phẩm vào giỏ hàng thành công", null));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi thêm sản phẩm vào giỏ hàng"));
        }
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<?>> removeToCart(@PathVariable Long cartItemId, @AuthenticationPrincipal AccountDetails accountDetails) {
        try {
            Long userId = accountDetails.getAccount().getUser().getId();
            System.out.println(userId);
            cartService.removeToCart(cartItemId, userId);
            return ResponseEntity.ok(ApiResponse.ok("Xóa sản phẩm khỏi giỏ hàng thành công", null));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("Sản phẩm không tồn tại trong giỏ hàng"));
        }
        catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.fail("Bạn không có quyền thực hiện hành động này"));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi xóa sản phẩm khỏi giỏ hàng"));
        }
    }
}
