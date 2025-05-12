package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.entity.FlashSale;
import org.api.grocerystorebackend.enums.FlashSaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlashSaleRepository extends JpaRepository<FlashSale, Long> {

    List<FlashSale> findByStatusAndStartTimeBefore(FlashSaleStatus status, LocalDateTime startTimeBefore);

    List<FlashSale> findByStatusAndEndTimeBefore(FlashSaleStatus status, LocalDateTime now);

    @Query("SELECT DISTINCT fl FROM FlashSale fl JOIN FETCH fl.flashSaleItems WHERE fl.status = :status")
    List<FlashSale> findAllTakingPlace(@Param("status") FlashSaleStatus status);
    @Query("""
        SELECT fs FROM FlashSale fs
        WHERE fs.endTime >= :now
          AND fs.status IN (:statuses)
        ORDER BY fs.endTime DESC
    """)
    List<FlashSale> findConflictingFlashSales(@Param("now") LocalDateTime now,
                                              @Param("statuses") List<FlashSaleStatus> statuses);


}
