package com.online_auction.product_service.dto;

import lombok.Data;

@Data
public class BidResponse {

    private Long id;
    private Double amount;
    private String bidderEmail;
}
