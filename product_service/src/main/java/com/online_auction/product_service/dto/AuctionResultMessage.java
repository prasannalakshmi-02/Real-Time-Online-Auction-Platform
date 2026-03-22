package com.online_auction.product_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionResultMessage {

    private Long productId;
    private String productName;
    private String sellerEmail;
    private String winnerEmail; // Null if unsold
    private Double finalPrice;
    private String status;
}
