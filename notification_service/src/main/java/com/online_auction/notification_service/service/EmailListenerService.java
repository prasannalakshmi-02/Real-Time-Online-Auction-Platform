package com.online_auction.notification_service.service;

import com.online_auction.notification_service.config.RabbitMQConfig;
import com.online_auction.notification_service.dto.AuctionResultMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailListenerService {

    @Autowired
    private JavaMailSender mailSender;

    @RabbitListener(queues = "auction.result.queue")
    public void receiveAuctionResult(AuctionResultMessage result) {
        System.out.println("📩 Received Auction Result for: " + result.getProductName());


            if (result.getSellerEmail() == null) {
                System.err.println("❌ Error: Seller email is null for Product #" + result.getProductId());
                return;
            }

            if ("SOLD".equalsIgnoreCase(result.getStatus())) {
                if (result.getWinnerEmail() == null) {
                    System.err.println("❌ Error: Product is SOLD but Winner email is null!");
                    return;
                }
                sendWinnerEmail(result);
                sendSellerSuccessEmail(result);
            } else {
                sendSellerUnsoldEmail(result);
            }


    }


    private void sendWinnerEmail(AuctionResultMessage result) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(result.getWinnerEmail());
        message.setSubject("🎉 Congratulations! You won the auction for " + result.getProductName());
        message.setText("Hello,\n\nYour bid of ₹" + result.getFinalPrice() + " was the highest! " +
                "Please log in to complete your purchase.");
        mailSender.send(message);
    }


    private void sendSellerSuccessEmail(AuctionResultMessage result) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(result.getSellerEmail());
        message.setSubject("💰 Your item '" + result.getProductName() + "' has been SOLD!");
        message.setText("Great news! Your item sold for ₹" + result.getFinalPrice() + ".");
        mailSender.send(message);
    }


    private void sendSellerUnsoldEmail(AuctionResultMessage result) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(result.getSellerEmail());
        message.setSubject("Update: Your auction for '" + result.getProductName() + "' has ended");
        message.setText("Hello,\n\nUnfortunately, no bids were placed. You can relist your item anytime.");
        mailSender.send(message);
    }
}
