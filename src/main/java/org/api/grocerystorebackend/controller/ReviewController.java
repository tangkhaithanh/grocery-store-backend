package org.api.grocerystorebackend.controller;

import org.api.grocerystorebackend.dto.request.ReviewRequest;
import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.ReviewDTO;
import org.api.grocerystorebackend.entity.User;
import org.api.grocerystorebackend.repository.OrderRepository;
import org.api.grocerystorebackend.security.AccountDetails;
import org.api.grocerystorebackend.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private IReviewService reviewService;

    @Autowired
    private OrderRepository orderRepository;

    // Thêm đánh giá:
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> addReview(@RequestParam Long orderItemId,
                                                    @RequestBody ReviewRequest request,
                                                    @AuthenticationPrincipal AccountDetails accountDetails) {
        try {
            User user = accountDetails.getAccount().getUser();
            reviewService.createReview(orderItemId, user, request);
            return ResponseEntity.ok(ApiResponse.ok("Đánh giá thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(e.getMessage()));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<?>> getReviewsByProduct(@PathVariable Long productId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int size,
                                                              @AuthenticationPrincipal AccountDetails accountDetails) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<ReviewDTO> reviews = reviewService.getReviewsByProduct(productId, pageable);
            return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách đánh giá thành công", reviews));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail("Lỗi khi lấy đánh giá: " + e.getMessage()));
        }
    }
}

