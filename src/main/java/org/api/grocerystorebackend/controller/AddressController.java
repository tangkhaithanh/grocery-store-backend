package org.api.grocerystorebackend.controller;
import org.api.grocerystorebackend.dto.request.AddressRequest;
import org.api.grocerystorebackend.dto.response.AddressDTO;
import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.security.AccountDetails;
import org.api.grocerystorebackend.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private IAddressService addressService;

    @GetMapping("/get-all-addresses")
    public ResponseEntity<ApiResponse<?>> getCurrentUserAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal AccountDetails accountDetails
    ) {
        try {
            Long userID = accountDetails.getAccount().getUser().getId();
            Pageable pageable = PageRequest.of(page, size);

            Page<AddressDTO> addressPage = addressService.getAllAddressesByUserId(userID, pageable);
            return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách địa chỉ thành công", addressPage));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy danh sách địa chỉ: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getAddressById(
            @PathVariable Long id,
            @AuthenticationPrincipal AccountDetails accountDetails
    ){
        try{
            Long userID = accountDetails.getAccount().getUser().getId();
            AddressDTO addressDTO = addressService.getAddressById(id);

            if (addressDTO != null && addressDTO.getUserId().equals(userID)) {
                return ResponseEntity.ok(ApiResponse.ok("Lấy thông tin địa chỉ thành công", addressDTO));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail("Không tìm thấy địa chỉ với ID = " + id));
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy thông tin địa chỉ: " + e.getMessage()));
        }
    }

    @PostMapping("/create-address")
    public ResponseEntity<ApiResponse<?>> createAddress(
            @RequestBody AddressRequest addressRequest,
            @AuthenticationPrincipal AccountDetails accountDetails
    )
    {
        try {
            Long userID = accountDetails.getAccount().getUser().getId();
            addressService.createAddress(userID, addressRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Thêm địa chỉ mới thành công", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi thêm địa chỉ mới: " + e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<?>> updateAddress(
            @PathVariable Long id,
            @RequestBody AddressRequest addressRequest
    ) {
        try {
            boolean updated = addressService.updateAddress(id, addressRequest);
            if (updated) {
                return ResponseEntity.ok(ApiResponse.ok("Cập nhật địa chỉ thành công", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail("Không tìm thấy địa chỉ với ID = " + id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi cập nhật địa chỉ: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteAddress(
            @PathVariable Long id
    ) {
        try {
            boolean isDeleted = addressService.deleteAddress(id);
            if (isDeleted) {
                return ResponseEntity.ok(ApiResponse.ok("Xóa địa chỉ thành công", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.fail("Không thể xóa địa chỉ với ID = " + id + ". Có thể đây là địa chỉ duy nhất, địa chỉ mặc định, hoặc không tồn tại."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi xóa địa chỉ: " + e.getMessage()));
        }
    }
    @PatchMapping("/set-default/{id}")
    public ResponseEntity<ApiResponse<?>> setDefaultAddress(
            @PathVariable Long id
    ) {
        try {
            boolean isSet = addressService.setDefaultAddress(id);
            if (isSet) {
                return ResponseEntity.ok(ApiResponse.ok("Đặt địa chỉ mặc định thành công", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail("Không tìm thấy địa chỉ với ID = " + id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi đặt địa chỉ mặc định: " + e.getMessage()));
        }
    }
}
