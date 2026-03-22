package com.online_auction.bidding_service.controller;

import com.online_auction.bidding_service.dto.BidRequest;
import com.online_auction.bidding_service.dto.BidResponse;
import com.online_auction.bidding_service.entity.Bid;
import com.online_auction.bidding_service.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<BidResponse> placeBid(@RequestBody BidRequest request, Principal principal){
        Bid savedBid = bidService.createBid(request.getProductId(), request.getAmount(), principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(bidService.mapToDto(savedBid));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<BidResponse>> getBidHistory(@PathVariable Long productId) {
        List<BidResponse> history = bidService.getBidHistoryForProduct(productId)
                .stream()
                .map(bidService::mapToDto)
                .toList();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{productId}/highest")
    public ResponseEntity<BidResponse> getHighestBid(@PathVariable Long productId) {

        Optional<Bid> highestBidOptional = bidService.getHighestBid(productId);
        Bid highestBid = highestBidOptional.get();
        return ResponseEntity.ok(bidService.mapToDto(highestBid));
    }
}
