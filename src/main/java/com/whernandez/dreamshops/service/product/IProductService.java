package com.whernandez.dreamshops.service.product;

import java.util.List;

import com.whernandez.dreamshops.dto.AddProductDto;
import com.whernandez.dreamshops.dto.ProductDto;
import com.whernandez.dreamshops.dto.UpdateProductDto;
import com.whernandez.dreamshops.model.Product;

public interface IProductService {

    Product addProduct(AddProductDto product);

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProduct(UpdateProductDto product, Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategoryId(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String  category, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String category, String name);

    Long countProductsByBrandAndName(String brand, String name);

    ProductDto convertToDto(Product product);

    List<ProductDto> getConvertedProducts(List<Product> products);

}
