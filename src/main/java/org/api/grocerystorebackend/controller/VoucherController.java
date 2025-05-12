package org.api.grocerystorebackend.controller;


import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.entity.Category;
import org.api.grocerystorebackend.entity.Voucher;
import org.api.grocerystorebackend.service.IVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {
    @Autowired
    IVoucherService voucherService;
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getVouchers() {
        try{
            List<Voucher> vouchers = voucherService.getAllVoucher();
            return ResponseEntity.ok(ApiResponse.ok("Lấy danh tất cả voucher thành công", vouchers));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy tất cả voucher"));
        }
    }
}
