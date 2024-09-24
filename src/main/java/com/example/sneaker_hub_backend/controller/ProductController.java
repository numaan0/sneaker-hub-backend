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

    @PostMapping
    public ResponseEntity<Product> createProduct(
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
            return ResponseEntity.badRequest().body(null);
        }
        AppUser seller = sellerOptional.get();

        // Handle image upload
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = fileUploadService.uploadFile(sellerId + "/products", "product_" + System.currentTimeMillis() + ".jpg", image);
        }

        // Create a new product
        Product product = new Product(name, description, category, new BigDecimal(price), imageUrl, Integer.parseInt(stock),"",0,0,0 ,seller,new BigDecimal(0));
        Product savedProduct = productService.saveProduct(product);

        return ResponseEntity.ok(savedProduct);
    }

    // POST method for updating existing products with an id
    @PostMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("price") String price,
            @RequestParam("stock") String stock,
            @RequestParam("seller_id") Long sellerId,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Optional<AppUser> sellerOptional = appUserService.getAppUserById(sellerId);
        if (!sellerOptional.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        AppUser seller = sellerOptional.get();

        Optional<Product> existingProductOptional = productService.getProductById(id);
        if (!existingProductOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Product product = existingProductOptional.get();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(new BigDecimal(price));
        product.setStock(Integer.parseInt(stock));

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileUploadService.uploadFile(sellerId + "/products", "product_" + System.currentTimeMillis() + ".jpg", image);
            product.setImageUrl(imageUrl);
        }

        Product updatedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(updatedProduct);
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

    @GetMapping("/related/{category}/{id}")
    public ResponseEntity<List<Product>> getRelatedProducts(@PathVariable String category, @PathVariable Long id) {
        List<Product> relatedProducts = productService.getRelatedProducts(category, id);
        return ResponseEntity.ok(relatedProducts);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = productService.getDistinctCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }
}
