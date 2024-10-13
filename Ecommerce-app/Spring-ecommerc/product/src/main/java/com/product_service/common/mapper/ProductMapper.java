package com.product_service.common.mapper;

import com.product_service.dto.ProductDTO;
import com.product_service.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static ProductDTO convertToDTO(Product product) {
        if (product == null) {
            return null;
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setDescription(product.getDescription());
        productDTO.setCreatedDate(product.getCreatedDate());
        productDTO.setUpdatedDate(product.getUpdatedDate());
        return productDTO;
    }

    public static Product convertToEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }
        Product product = new Product();
        product.setProductId(productDTO.getProductId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setDescription(productDTO.getDescription());
        product.setCreatedDate(productDTO.getCreatedDate());
        product.setUpdatedDate(productDTO.getUpdatedDate());
        return product;
    }
}
