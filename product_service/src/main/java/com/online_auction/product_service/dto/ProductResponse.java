package com.online_auction.product_service.dto;

import com.online_auction.product_service.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Double basePrice;
    private LocalDateTime auctionEndTime;
    private String sellerEmail;
    private ProductStatus status;
}
