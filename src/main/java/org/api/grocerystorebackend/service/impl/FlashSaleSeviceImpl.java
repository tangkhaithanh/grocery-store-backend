package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.dto.response.FlashSaleDTO;
import org.api.grocerystorebackend.entity.FlashSale;
import org.api.grocerystorebackend.entity.FlashSaleItem;
import org.api.grocerystorebackend.entity.OrderItem;
import org.api.grocerystorebackend.entity.Product;
import org.api.grocerystorebackend.enums.FlashSaleStatus;
import org.api.grocerystorebackend.mapper.FlashSaleMapper;
import org.api.grocerystorebackend.repository.FlashSaleItemRepository;
import org.api.grocerystorebackend.repository.FlashSaleRepository;
import org.api.grocerystorebackend.repository.ProductRepository;
import org.api.grocerystorebackend.service.IFlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlashSaleSeviceImpl implements IFlashSaleService {
    @Autowired
    FlashSaleItemRepository flashSaleItemRepository;

    @Autowired
    FlashSaleRepository flashSaleRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    FlashSaleMapper flashSaleMapper;
    @Override
    public Optional<FlashSaleItem> findProductInFlashSale(Long productId) {
        return flashSaleItemRepository.findProductInFL(productId, LocalDateTime.now());
    }

    @Scheduled(fixedRate = 60000) // Mỗi 60 giây
    @Override
    public void activeFlashSales() {
        LocalDateTime now = LocalDateTime.now();
        List<FlashSale> flashSales = flashSaleRepository
                .findByStatusAndStartTimeBefore(FlashSaleStatus.UPCOMING, now);

        for(FlashSale flashSale : flashSales) {
            flashSale.setStatus(FlashSaleStatus.ACTIVE);
        }

        if(!flashSales.isEmpty()) {
            System.out.println("Active Flash Sales");
            flashSaleRepository.saveAll(flashSales);
        }

    }

    @Scheduled(fixedRate = 60000) // Mỗi 60 giây
    @Override
    public void expireFlashSales() {
        LocalDateTime now = LocalDateTime.now();
        List<FlashSale> flashSales = flashSaleRepository
                .findByStatusAndEndTimeBefore(FlashSaleStatus.ACTIVE, now);

        for(FlashSale flashSale : flashSales) {
            flashSale.setStatus(FlashSaleStatus.ENDED);
        }

        if(!flashSales.isEmpty()) {
            System.out.println("End Flash Sales");
            flashSaleRepository.saveAll(flashSales);
        }
    }

    @Transactional
    @Override
    public FlashSaleDTO createProductInFlashSale() {
        LocalDateTime now = LocalDateTime.now();

        // Lấy danh sách FlashSale đang còn hiệu lực (ACTIVE hoặc UPCOMING)
        List<FlashSale> conflictingSales = flashSaleRepository.findConflictingFlashSales(
                now,
                List.of(FlashSaleStatus.ACTIVE, FlashSaleStatus.UPCOMING)
        );

        // Mặc định: bắt đầu sau 2 phút
        LocalDateTime startTime = now.plusMinutes(2);

        if (!conflictingSales.isEmpty()) {
            // Lấy thời gian kết thúc mới nhất của các flash sale đang hoạt động/upcoming
            LocalDateTime latestEndTime = conflictingSales.get(0).getEndTime();

            if (!startTime.isAfter(latestEndTime)) {
                startTime = latestEndTime.plusMinutes(1); // Dời đến sau 1 phút
            }
        }

        LocalDateTime endTime = startTime.plusHours(2);

        String name = "Flash Sale " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String description = "Khuyến mãi tự động từ hệ thống dựa trên dữ liệu yêu thích và đánh giá.";

        FlashSale flashSale = new FlashSale();
        flashSale.setName(name);
        flashSale.setDescription(description);
        flashSale.setStartTime(startTime);
        flashSale.setEndTime(endTime);
        flashSale.setStatus(FlashSaleStatus.UPCOMING);
        flashSale.setCreatedAt(now);
        flashSale.setUpdatedAt(now);

        flashSale = flashSaleRepository.save(flashSale);

        // Tìm sản phẩm đủ điều kiện
        Pageable pageable = PageRequest.of(0, 20);
        List<Product> products = productRepository.findFeatureProductsForFlashSale(pageable);

        List<FlashSaleItem> flashSaleItems = new ArrayList<>();
        for (Product product : products) {
            FlashSaleItem item = new FlashSaleItem();
            item.setFlashSale(flashSale);
            item.setProduct(product);
            item.setFlashSalePrice(product.getPrice().multiply(BigDecimal.valueOf(0.8)));

            int soldQty = product.getOrderItems().stream().mapToInt(OrderItem::getQuantity).sum();
            int stockQty = product.getQuantity() - soldQty;

            item.setStockQuantity(stockQty);
            item.setSoldQuantity(0);
            item.setMaxPerCustomer(2);
            flashSaleItems.add(item);
            flashSaleItemRepository.save(item);
        }
        flashSale.setFlashSaleItems(flashSaleItems);
        return flashSaleMapper.toDTO(flashSale);
    }


    @Override
    public List<FlashSaleDTO> getFlashSaleList() {
        // Lấy tất cả các FlashSale có trạng thái ACTIVE
        List<FlashSale> flashSales = flashSaleRepository.findAllTakingPlace(FlashSaleStatus.ACTIVE);
        // Chuyển đổi danh sách FlashSale thành FlashSaleDTO
        return flashSales.stream()
                .map(flashSaleMapper::toDTO)  // Chuyển từng FlashSale sang DTO
                .collect(Collectors.toList());  // Chuyển đổi thành danh sách
    }

    @Override
    public FlashSaleDTO getFlashSaleByIdFlashSaleItem(Long id) {
        FlashSaleItem fli = flashSaleItemRepository.findById(id).orElse(null);
        if(fli != null) {
            FlashSale fl = flashSaleRepository.findById(fli.getFlashSale().getId()).orElse(null);
            if(fl != null) {
                return flashSaleMapper.toDTO(fl);
            }
            return null;
        }
        return null;
    }

}
