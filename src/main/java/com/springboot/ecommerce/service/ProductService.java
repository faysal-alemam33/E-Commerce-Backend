package com.springboot.ecommerce.service;

import com.springboot.ecommerce.dto.ProductRequest;
import com.springboot.ecommerce.dto.ProductResponse;
import com.springboot.ecommerce.entity.Category;
import com.springboot.ecommerce.entity.Product;
import com.springboot.ecommerce.repository.CategoryRepository;
import com.springboot.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    private static final String UPLOAD_DIR = "uploads";



    @Transactional
    public ProductResponse createProduct(ProductRequest request, MultipartFile image) throws IOException {
        Product product = new Product();
        product.setName(request.getName());
        product.setUnit(request.getUnit());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());

        if (image != null) {
            String imageUrl = saveImage(image);
            product.setImageUrl(imageUrl);
        }

        if (request.getCategoryIds() != null) {
            Set<Category> categories = request.getCategoryIds().stream()
                    .map(id -> categoryRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Category not found: " + id)))
                    .collect(Collectors.toSet());
            product.setCategories(categories);
        }

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
        product.setActive(false);
        productRepository.save(product);
    }

    public Page<ProductResponse> listProducts(int offset, int pageSize, String sortType) {
        Sort.Direction direction = Sort.Direction.fromString(sortType);
        // PageRequest (page number to fetch, number of items, Sorting criteria)
        PageRequest pageRequest = PageRequest.of(offset, pageSize, Sort.by(direction, "id"));

        return productRepository.findAll(pageRequest).map(this::mapToResponse);
    }


    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Long randomLong = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        // Combine the random Long with the original filename
        String filename = randomLong + "-" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        // Copy the file to the target location
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setUnit(product.getUnit());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setActive(product.isActive());
        response.setImageUrl(product.getImageUrl());
        response.setCategories(product.getCategories());
        return response;
    }


}
