package org.api.grocerystorebackend.controller;

import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.FlashSaleDTO;
import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.entity.FlashSale;
import org.api.grocerystorebackend.service.IFlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flash-sale")
public class FlashSaleController {
    @Autowired
    IFlashSaleService flashSaleService;

    @PostMapping("/auto-create")
    public ResponseEntity<ApiResponse<?>> createAutoFlashSale() {
        try {
            FlashSaleDTO flashSale = flashSaleService.createProductInFlashSale();
            return ResponseEntity.ok(ApiResponse.ok("Tạo Flash Sale thành công", flashSale));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi tạo Flash Sale"));
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getFlashSales() {
        try {
            List<FlashSaleDTO> flashSaleList = flashSaleService.getFlashSaleList();
            return ResponseEntity.ok(ApiResponse.ok("Lấy Flash Sale thành công", flashSaleList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy Flash Sale"));
        }
    }

    @GetMapping("/fli")
    public ResponseEntity<ApiResponse<?>> getFlashSale(
            @RequestParam("flash_sale_item_id") Long id
    ) {
        try {
            FlashSaleDTO flashSale = flashSaleService.getFlashSaleByIdFlashSaleItem(id);
            return ResponseEntity.ok(ApiResponse.ok("Lấy Flash Sale thành công", flashSale));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy Flash Sale"));
        }
    }
}
