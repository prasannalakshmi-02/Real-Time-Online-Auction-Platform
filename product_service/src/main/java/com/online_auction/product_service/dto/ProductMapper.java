package com.online_auction.product_service.dto;

import com.online_auction.product_service.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toResponseDto(Product product);

    Product toEntity(ProductRequest request);

}
