package com.online_auction.bidding_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidResponse {

    private Long id;
    private Long productId;
    private Double amount;
    private LocalDateTime bidTime;
    private String bidderName;
    private String bidderEmail;
}
