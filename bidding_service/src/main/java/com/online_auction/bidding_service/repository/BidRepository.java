package com.online_auction.bidding_service.repository;

import com.online_auction.bidding_service.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    Optional<Bid> findTopByProductIdOrderByAmountDesc(Long productId);
    List<Bid> findByProductIdOrderByAmountDesc(Long productId);


}
