package com.online_auction.product_service.repository;

import com.online_auction.product_service.entity.Product;
import com.online_auction.product_service.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findBySellerEmail(String sellerEmail);
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByAuctionEndTimeBeforeAndStatus(LocalDateTime currentTime, ProductStatus status);

}
