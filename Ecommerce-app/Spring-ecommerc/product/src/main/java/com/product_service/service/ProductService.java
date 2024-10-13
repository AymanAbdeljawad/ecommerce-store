package com.product_service.service;

import com.product_service.common.constants.Constants;
import com.product_service.common.mapper.ProductMapper;
import com.product_service.dto.*;
import com.product_service.entity.Product;
import com.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseProduct createProduct(RequestProduct requestProduct) {
        ProductDTO productDTO = requestProduct.getProductDTO();
        if (productDTO == null) {
            return new ResponseProduct(Constants.VALIDATION_ERROR_CODE, "1", "ProductDTO cannot be null.", Constants.VALIDATION_ERROR_MESSAGE, null);
        }
        if (!StringUtils.hasText(productDTO.getName())) {
            return new ResponseProduct(Constants.VALIDATION_ERROR_CODE, "1", "Product name cannot be null or empty.", Constants.VALIDATION_ERROR_MESSAGE, null);
        }
        if (productDTO.getPrice() == null || productDTO.getPrice() <= 0) {
            return new ResponseProduct(Constants.VALIDATION_ERROR_CODE, "1", "Product price must be a positive value.", Constants.VALIDATION_ERROR_MESSAGE, null);
        }
        Product product = ProductMapper.convertToEntity(productDTO);
        try {
            Product savedProduct = productRepository.save(product);
            ProductDTO savedProductDTO = ProductMapper.convertToDTO(savedProduct);
            return new ResponseProduct(Constants.SUCCESS_CODE, "0", Constants.SUCCESS_MESSAGE, Constants.SUCCESS_MESSAGE, savedProductDTO);
        } catch (DataIntegrityViolationException e) {
            return new ResponseProduct(Constants.VALIDATION_ERROR_CODE, "1", "Data integrity issue with provided data.", Constants.VALIDATION_ERROR_MESSAGE, null);
        } catch (Exception e) {
            return new ResponseProduct(Constants.INTERNAL_ERROR_CODE, "1", Constants.INTERNAL_ERROR_MESSAGE, Constants.INTERNAL_ERROR_MESSAGE, null);
        }
    }
    public ResponseAllProduct getAllProducts() {
        try {
            List<ProductDTO> productDTOS = productRepository.findAll()
                    .stream()
                    .map(ProductMapper::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseAllProduct(Constants.SUCCESS_CODE, "0", "Products retrieved successfully.", "Products retrieved successfully", productDTOS);
        } catch (Exception e) {
            return new ResponseAllProduct(Constants.VALIDATION_ERROR_CODE, "1", "Failed to retrieve products. Please try again later.", "Error occurred", null);
        }
    }
    public ResponseProduct getProductById(RequestProductIdDTO requestProductIdDTO) {
        Long productId = requestProductIdDTO.getProductId();
        ProductDTO productDTO;
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isEmpty()) {
                return new ResponseProduct(Constants.VALIDATION_ERROR_CODE, "1", "Product not found with id " + productId, "Product not found");
            }
            Product product = optionalProduct.get();
            productDTO = ProductMapper.convertToDTO(product);
            return new ResponseProduct(Constants.SUCCESS_CODE, "0", "Product retrieved successfully.", "Product retrieved successfully", productDTO);
        } catch (RuntimeException e) {
            return new ResponseProduct(Constants.VALIDATION_ERROR_CODE, "1", "Failed to retrieve product. Please try again later.", "Error occurred");
        }
    }


    public ResponseProduct updateProduct(RequestProduct requestProduct) {
        ProductDTO productDTO = requestProduct.getProductDTO();
        Long id = productDTO.getProductId();
        try {
            if (!productRepository.existsById(id)) {
                return new ResponseProduct(Constants.VALIDATION_ERROR_CODE, "1", "Product not found with ID " + id, "Product not found", null);
            }
            Product product = ProductMapper.convertToEntity(productDTO);
            product.setProductId(id);
            Product updatedProduct = productRepository.save(product);
            ProductDTO updatedProductDTO = ProductMapper.convertToDTO(updatedProduct);
            return new ResponseProduct(Constants.SUCCESS_CODE, "0", "Product updated successfully.", "Product updated successfully", updatedProductDTO);
        } catch (Exception e) {
            return new ResponseProduct(1, "1", "Failed to update product. Please try again later.", "Error occurred", null);
        }
    }

    @Transactional
    public ResponseProduct updateQuantityList(RequestUpdateQuantity requestUpdateQuantity) {
        try {
            List<QuantityDTO> quantityDTOList = requestUpdateQuantity.getQuantityDTOList();
            StringBuilder errorMessages = new StringBuilder();
            boolean hasErrors = false;
            List<String> productNames = quantityDTOList.stream()
                    .map(QuantityDTO::getProductName)
                    .collect(Collectors.toList());
            List<Product> products = productRepository.findByNameIn(productNames);
            Map<String, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getName, product -> product));
            for (QuantityDTO quantityDTO : quantityDTOList) {
                String productName = quantityDTO.getProductName();
                Product product = productMap.get(productName);
                if (product == null) {
                    errorMessages.append("Product not found with name ").append(productName).append(". ");
                    hasErrors = true;
                    continue;
                }
                Integer requestedQuantity = quantityDTO.getQuantity();
                Integer availableQuantity = product.getQuantity();

                if (requestedQuantity > availableQuantity) {
                    errorMessages.append("Insufficient quantity for product name ").append(productName)
                            .append(". Requested: ").append(requestedQuantity)
                            .append(", Available: ").append(availableQuantity).append(". ");
                    hasErrors = true;
                } else {
                    product.setQuantity(availableQuantity - requestedQuantity);
                }
            }
            if (hasErrors) {
                return new ResponseProduct(Constants.VALIDATION_ERROR_CODE, "1", errorMessages.toString(), "Errors occurred", null);
            }
            productRepository.saveAll(products);
            List<ProductDTO> updatedProductDTOs = products.stream()
                    .map(ProductMapper::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseProduct(Constants.SUCCESS_CODE, "0", "Products updated successfully.", "Products updated successfully");

        } catch (Exception e) {
            return new ResponseProduct(Constants.VALIDATION_ERROR_CODE, "1", "Failed to update products. Please try again later.", "Error occurred");
        }
    }


    public ResponseProduct deleteProduct(RequestProductIdDTO requeestByIDProductDTO) {
        Long id = requeestByIDProductDTO.getProductId();
        try {
            if (!productRepository.existsById(id)) {
                return new ResponseProduct(Constants.VALIDATION_ERROR_CODE, "1", "Product not found with ID " + id, "Product not found", null);
            }
            productRepository.deleteById(id);
            return new ResponseProduct(Constants.SUCCESS_CODE, "0", "Product deleted successfully.", "Product deleted successfully", null);
        } catch (Exception e) {
            return new ResponseProduct(Constants.VALIDATION_ERROR_CODE, "1", "Failed to delete product. Please try again later.", "Error occurred", null);
        }
    }
}
