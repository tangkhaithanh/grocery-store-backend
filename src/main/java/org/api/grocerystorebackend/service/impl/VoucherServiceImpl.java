package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.entity.Voucher;
import org.api.grocerystorebackend.repository.VoucherRepository;
import org.api.grocerystorebackend.service.IVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherServiceImpl implements IVoucherService {
    @Autowired
    VoucherRepository voucherRepository;
    @Override
    public List<Voucher> getAllVoucher() {
        return voucherRepository.findAll();
    }
}
