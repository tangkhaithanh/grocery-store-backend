package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.response.OrderDTO;
import org.api.grocerystorebackend.entity.Voucher;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IVoucherService {
    List<Voucher> getAllVoucher();
}
