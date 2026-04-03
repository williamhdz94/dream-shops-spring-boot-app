package com.whernandez.dreamshops.service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.whernandez.dreamshops.dto.AddProductDto;
import com.whernandez.dreamshops.dto.ImageDto;
import com.whernandez.dreamshops.dto.ProductDto;
import com.whernandez.dreamshops.dto.UpdateProductDto;
import com.whernandez.dreamshops.exceptions.ProductNotFoundException;
import com.whernandez.dreamshops.model.Category;
import com.whernandez.dreamshops.model.Image;
import com.whernandez.dreamshops.model.Product;
import com.whernandez.dreamshops.repository.CategoryRepository;
import com.whernandez.dreamshops.repository.ImageRepository;
import com.whernandez.dreamshops.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductDto product) {
        //  check if the category is found in the DB
        // if yes, set it as the new product using category
        // if no, the save it as a new category
        // the set as the new product category

        Category category = Optional.ofNullable(categoryRepository.findByName(product.getCategory().getName()))
            .orElseGet(() -> {
                Category newCategory = new Category(product.getCategory().getName());
                return categoryRepository.save(newCategory);
            });

        product.setCategory(category);

        return productRepository.save(createProduct(product, category));
    }

    private Product createProduct(AddProductDto request, Category category) {
       return new Product(
        request.getName(),
        request.getBrand(),
        request.getPrice(),
        request.getInventory(),
        request.getDescription(),
        category
       );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ProductNotFoundException("Product not found!");}
        );
    }

    @Override
    public Product updateProduct(UpdateProductDto product, Long productId) {
        return productRepository.findById(productId)
            .map(existingProduct -> updateExistingProduct(existingProduct, product))
            .map(productRepository:: save)
            .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductDto request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        
        existingProduct.setCategory(category);

        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategoryId(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
       return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imagesDtos = images.stream()
            .map(image -> modelMapper.map(image, ImageDto.class))
            .toList();

        productDto.setImages(imagesDtos);
        return productDto;
    }

}
