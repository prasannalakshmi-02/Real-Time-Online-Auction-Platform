package com.online_auction.bidding_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductDto {

    private Long id;
    private String name;
    private Double basePrice;
    private LocalDateTime auctionEndTime;
}
