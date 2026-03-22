package com.online_auction.bidding_service.service;

import com.online_auction.bidding_service.client.ProductClient;
import com.online_auction.bidding_service.dto.BidResponse;
import com.online_auction.bidding_service.dto.ProductDto;
import com.online_auction.bidding_service.entity.Bid;
import com.online_auction.bidding_service.repository.BidRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final ProductClient productClient;
    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public Bid createBid(Long productId, double bidAmount, String bidderEmail){
        ProductDto product = productClient.getProductById(productId);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime biddingEndTime = product.getAuctionEndTime();
        Optional<Bid> prevBid = bidRepository.findTopByProductIdOrderByAmountDesc(productId);

        if(currentTime.isAfter(biddingEndTime)){
            throw new IllegalArgumentException("Auction has already ended");
        }
        if(bidAmount <= product.getBasePrice()) {
            throw new IllegalArgumentException("Bid amount must be strictly greater than the base price of $" + product.getBasePrice());
        }
        if (prevBid.isPresent()) {
            double currentHighest = prevBid.get().getAmount();
            if (bidAmount <= currentHighest) {
                throw new IllegalArgumentException("Bid must be higher than the current highest bid of $" + currentHighest);
            }
        }
        Bid newBid = new Bid();
        newBid.setProductId(productId);
        newBid.setAmount(bidAmount);
        newBid.setBidTime(LocalDateTime.now());
        newBid.setBidderEmail(bidderEmail);

        Bid savedBid = bidRepository.save(newBid);

        String message = "New bid of $" + bidAmount + " placed on Product ID " + productId + " by " + bidderEmail;
        rabbitTemplate.convertAndSend("email_queue", message);

        messagingTemplate.convertAndSend("/topic/bids/" + productId, savedBid);

        return savedBid;
    }

    public List<Bid> getBidHistoryForProduct(Long productId){
        return bidRepository.findByProductIdOrderByAmountDesc(productId);
    }

    public Optional<Bid> getHighestBid(Long productId){
        Optional<Bid> bid = bidRepository.findTopByProductIdOrderByAmountDesc(productId);
        if(bid.isPresent()){
            return bid;
        }else{
            throw new RuntimeException("No bids found for product ID:" + productId);
        }
    }

    public BidResponse mapToDto(Bid bid) {
        BidResponse dto = new BidResponse();
        dto.setId(bid.getId());
        dto.setProductId(bid.getProductId());
        dto.setAmount(bid.getAmount());
        dto.setBidTime(bid.getBidTime());
        dto.setBidderEmail(bid.getBidderEmail());

        String email = bid.getBidderEmail();
        String maskedEmail = email.substring(0, 1) + "***" + email.substring(email.indexOf("@"));
        dto.setBidderName(maskedEmail);

        return dto;
    }
}
