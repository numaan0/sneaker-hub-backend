package com.example.sneaker_hub_backend.service;

import com.example.sneaker_hub_backend.model.Product;
import com.example.sneaker_hub_backend.repository.OrderItemRepository;
import com.example.sneaker_hub_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private OrderItemRepository orderItemRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getProductsBySeller(Long sellerId) {
        return productRepository.findBySellerId(sellerId);  // Changed from findBySeller_id to findBySellerId
    }
    public List<Product> getRelatedProducts(String category, Long excludeId) {
        List<Product> relatedProducts = productRepository.findByCategory(category);
        relatedProducts.removeIf(product -> product.getId().equals(excludeId));
        return relatedProducts;
    }

    public List<String> getDistinctCategories() {
        return productRepository.findDistinctCategories();
    }
    public void deleteProductsBySellerId(Long sellerId) {
        // orderItemRepository.deleteByProductId(productId);
        List<Product> products = productRepository.findBySellerId(sellerId);

        for (Product product : products) {
            orderItemRepository.deleteByProductId(product.getId());
        }
        productRepository.deleteBySellerId(sellerId); 
    }
}
