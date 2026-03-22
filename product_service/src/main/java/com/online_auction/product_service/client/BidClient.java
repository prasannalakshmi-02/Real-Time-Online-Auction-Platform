package com.online_auction.product_service.client;

import com.online_auction.product_service.dto.BidResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "BIDDING-SERVICE")
public interface BidClient {

    @GetMapping("/bids/{productId}/highest")
    BidResponse getHighestBid(@PathVariable("productId") Long productId);

}
