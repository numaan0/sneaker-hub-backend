package com.example.sneaker_hub_backend.controller;

import com.example.sneaker_hub_backend.model.Product;
import com.example.sneaker_hub_backend.model.AppUser;
import com.example.sneaker_hub_backend.service.ProductService;
import com.example.sneaker_hub_backend.service.FileUploadService;
import com.example.sneaker_hub_backend.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/{id}")
    public ResponseEntity<Product> createOrUpdateProduct(
            @PathVariable(value = "id", required = false) Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("price") String price,
            @RequestParam("stock") String stock,
            @RequestParam("seller_id") Long sellerId,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
    
        // Fetch the seller
        Optional<AppUser> sellerOptional = appUserService.getAppUserById(sellerId);
        if (!sellerOptional.isPresent()) {
            return ResponseEntity.badRequest().body(null); // Handle the case where the seller is not found
        }
        AppUser seller = sellerOptional.get();
    
        // Handle image upload
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = fileUploadService.uploadFile(sellerId + "/products", "product_" + System.currentTimeMillis() + ".jpg", image);
        }
    
        // Check if updating an existing product
        Product product;
        if (id != null && id > 0) {
            Optional<Product> existingProductOptional = productService.getProductById(id);
            if (existingProductOptional.isPresent()) {
                product = existingProductOptional.get();
                // Update existing product's fields
                product.setName(name);
                product.setDescription(description);
                product.setCategory(category);
                product.setPrice(new BigDecimal(price));
                product.setStock(Integer.parseInt(stock));
                if (imageUrl != null) {
                    product.setImageUrl(imageUrl);
                }
            } else {
                // If the product doesn't exist, return a 404 response
                return ResponseEntity.notFound().build();
            }
        } else {
            // Create a new product
            product = new Product(name, description, category, new BigDecimal(price), imageUrl, Integer.parseInt(stock), seller);
        }
    
        Product savedProduct = productService.saveProduct(product);
    
        return ResponseEntity.ok(savedProduct);
    }
    

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Product>> getProductsBySeller(@PathVariable Long sellerId) {
        List<Product> products = productService.getProductsBySeller(sellerId);
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }
}
