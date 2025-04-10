package org.api.grocerystorebackend.controller;
import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.security.AccountDetails;
import org.api.grocerystorebackend.service.IFavouriteProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favourites")
public class FavouriteProductController {
    @Autowired
    private IFavouriteProductService favouriteProductService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<?>> getFavouriteProducts(
            @AuthenticationPrincipal AccountDetails accountDetails) {
        try {
            Long userId = accountDetails.getAccount().getUser().getId();
            List<ProductDTO> products = favouriteProductService.getFavourites(userId);
            return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách sản phẩm yêu thích thành công", products));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy danh sách sản phẩm yêu thích"));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> addFavouriteProduct(
            @RequestParam Long productId,
            @AuthenticationPrincipal AccountDetails accountDetails) {
        try {
            Long userId = accountDetails.getAccount().getUser().getId();
            favouriteProductService.addFavouriteProduct(productId,userId);
            return ResponseEntity.ok(ApiResponse.ok("Đã thêm sản phẩm vào danh sách yêu thích", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi thêm sản phẩm vào danh sách yêu thích"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> deleteFavouriteProduct(
            @RequestParam Long productId,
            @AuthenticationPrincipal AccountDetails accountDetails) {
        try {
            Long userId = accountDetails.getAccount().getUser().getId();
            favouriteProductService.deleteFavouriteProduct(productId, userId);
            return ResponseEntity.ok(ApiResponse.ok("Đã xóa sản phẩm khỏi danh sách yêu thích", null));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi xóa sản phẩm khỏi danh sách yêu thích"));
        }
    }
}
