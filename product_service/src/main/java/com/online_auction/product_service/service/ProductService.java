package com.online_auction.product_service.service;

import com.online_auction.product_service.dto.ProductMapper;
import com.online_auction.product_service.dto.ProductRequest;
import com.online_auction.product_service.dto.ProductResponse;
import com.online_auction.product_service.entity.Product;
import com.online_auction.product_service.entity.ProductStatus;
import com.online_auction.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse createProduct(ProductRequest request, String sellerEmail){

        Product product = productMapper.toEntity(request);
        product.setSellerEmail(sellerEmail);
        product.setStatus(ProductStatus.ACTIVE);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        return productMapper.toResponseDto(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request, String sellerEmail) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        if (!existingProduct.getSellerEmail().equals(sellerEmail)) {
            throw new RuntimeException("Unauthorized: You can only update your own products.");
        }

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setBasePrice(request.getBasePrice());
        existingProduct.setAuctionEndTime(request.getAuctionEndTime());

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toResponseDto(updatedProduct);
    }

    public void deleteProduct(Long id, String sellerEmail) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        if (!existingProduct.getSellerEmail().equals(sellerEmail)) {
            throw new RuntimeException("Unauthorized: You can only delete your own products.");
        }

        productRepository.delete(existingProduct);
    }

}
