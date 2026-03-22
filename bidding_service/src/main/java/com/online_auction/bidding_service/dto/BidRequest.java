package com.online_auction.bidding_service.dto;

import lombok.Data;

@Data
public class BidRequest {
    private Long productId;
    private Double amount;
}
