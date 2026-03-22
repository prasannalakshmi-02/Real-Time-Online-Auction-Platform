package com.online_auction.product_service.scheduler;

import com.online_auction.product_service.client.BidClient;
import com.online_auction.product_service.dto.AuctionResultMessage;
import com.online_auction.product_service.dto.BidResponse;
import com.online_auction.product_service.entity.Product;
import com.online_auction.product_service.entity.ProductStatus;
import com.online_auction.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionScheduler {

    private final ProductRepository productRepository;
    private final BidClient bidClient;
    private final RabbitTemplate rabbitTemplate;

    // A Cron expression: "0 * * * * *" means "Run at the 0th second of every minute"
    @Scheduled(cron = "0 * * * * *")
    public void checkAndCloseExpiredAuctions() {
        LocalDateTime now = LocalDateTime.now();
        List<Product> expiredAuctions = productRepository.findByAuctionEndTimeBeforeAndStatus(now, ProductStatus.ACTIVE);

        for (Product product : expiredAuctions) {
            AuctionResultMessage message = new AuctionResultMessage();
            message.setProductId(product.getId());
            message.setProductName(product.getName());
            message.setSellerEmail(product.getSellerEmail());

            try {
                BidResponse highestBidObj = bidClient.getHighestBid(product.getId());

                if (highestBidObj != null) {
                    product.setStatus(ProductStatus.SOLD);
                    message.setStatus("SOLD");
                    message.setWinnerEmail(highestBidObj.getBidderEmail());
                    message.setFinalPrice(highestBidObj.getAmount());
                } else {
                    product.setStatus(ProductStatus.UNSOLD);
                    message.setStatus("UNSOLD");
                }
                productRepository.save(product);
                rabbitTemplate.convertAndSend("auction.exchange", "auction.result", message);

                System.out.println("📤 Sent Auction Result for Product #" + product.getId() + " to RabbitMQ");

            } catch (Exception e) {
                System.err.println("❌ Error processing auction end for #" + product.getId());
            }
        }
    }

}