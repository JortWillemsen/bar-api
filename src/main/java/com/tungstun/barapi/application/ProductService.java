package com.tungstun.barapi.application;

import com.tungstun.barapi.data.SpringProductRepository;
import com.tungstun.barapi.domain.Category;
import com.tungstun.barapi.domain.bar.Bar;
import com.tungstun.barapi.domain.product.Product;
import com.tungstun.barapi.domain.product.ProductBuilder;
import com.tungstun.barapi.domain.product.ProductType;
import com.tungstun.barapi.presentation.dto.request.ProductRequest;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final SpringProductRepository SPRING_PRODUCT_REPOSITORY;
    private final BarService BAR_SERVICE;
    private final CategoryService CATEGORY_SERVICE;

    public ProductService(SpringProductRepository springProductRepository, BarService barService, CategoryService categoryService) {
        this.SPRING_PRODUCT_REPOSITORY = springProductRepository;
        this.BAR_SERVICE = barService;
        this.CATEGORY_SERVICE = categoryService;
    }

    private Product findProductInProducts(List<Product> products, Long productId) throws NotFoundException {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        throw new NotFoundException(String.format("No product found with id %s", productId));
    }

    private ProductType convertStringToProductType(String type) {
        ProductType productType = ProductType.OTHER;
        if (type != null) {
            try {
                productType = ProductType.valueOf(type.toUpperCase());
            }catch (Exception e) {
                throw new IllegalArgumentException(String.format("Invalid product type '%s'", type));
            }
        }
        return productType;
    }

    private Product saveProductForBar(Bar bar, Product product) {
        product = this.SPRING_PRODUCT_REPOSITORY.save(product);
        bar.addProduct(product);
        this.BAR_SERVICE.saveBar(bar);
        return product;
    }

    private List<Product> filterProductsByCategoryId(List<Product> products, Long categoryId) throws NotFoundException {
        ArrayList<Product> resProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().getId().equals(categoryId)) {
                resProducts.add(product);
            }
        }
        if (resProducts.isEmpty()) throw new NotFoundException(String.format("No products found of category with id %s", categoryId));
        return resProducts;
    }

    private List<Product> filterProductsByTypeString(List<Product> products, String type) throws NotFoundException {
        ProductType productType = convertStringToProductType(type);
        ArrayList<Product> resProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getProductType().equals(productType)) {
                resProducts.add(product);
            }
        }
        if (resProducts.isEmpty()) throw new NotFoundException(String.format("No products found of type %s", productType));
        return resProducts;
    }

    private List<Product> getAllProductsOfBar(Long barId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        return bar.getProducts();
    }

    public List<Product> getProductsOfBar(Long barId, String type, Long categoryId) throws NotFoundException {
        List<Product> products = getAllProductsOfBar(barId);
        if (type != null) products = filterProductsByTypeString(products, type);
        if (categoryId != null) products = filterProductsByCategoryId(products, categoryId);
        if (products.isEmpty()) throw new NotFoundException(String.format("No products found for bar with id %s", barId));
        return products;
    }

    public Product getProductOfBar(Long barId, Long productId) throws NotFoundException {
        List<Product> allProducts = getAllProductsOfBar(barId);
        return findProductInProducts(allProducts, productId);
    }
    private Product buildProduct(Long barId, ProductRequest productRequest) throws NotFoundException {
        Category category = this.CATEGORY_SERVICE.getCategoryOfBar(barId, productRequest.categoryId);
        ProductType productType = convertStringToProductType(productRequest.productType);
        return new ProductBuilder()
                .setName(productRequest.name)
                .setBrand(productRequest.brand)
                .setSize(productRequest.size)
                .setPrice(productRequest.price)
                .setFavorite(productRequest.isFavorite)
                .setCategory(category)
                .setProductType(productType)
                .build();
    }

    public Product addProductToBar(Long barId, ProductRequest productRequest) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Product product = buildProduct(barId, productRequest);
        return saveProductForBar(bar, product);
    }

    public Product updateProductOfBar(Long barId, Long productId, ProductRequest productRequest) throws NotFoundException {
        Product product = getProductOfBar(barId, productId);
        Category category = this.CATEGORY_SERVICE.getCategoryOfBar(barId, productRequest.categoryId);
        ProductType productType = convertStringToProductType(productRequest.productType);
        product.setName(productRequest.name);
        product.setBrand(productRequest.brand);
        product.setSize(productRequest.size);
        product.setPrice(productRequest.price);
        product.setFavorite(productRequest.isFavorite);
        product.setCategory(category);
        product.setProductType(productType);
        return this.SPRING_PRODUCT_REPOSITORY.save(product);
    }

    public void deleteProductOfBar(Long barId, Long productId) throws NotFoundException {
        Bar bar = this.BAR_SERVICE.getBar(barId);
        Product product = getProductOfBar(barId, productId);
        bar.removeProduct(product);
        this.BAR_SERVICE.saveBar(bar);
    }
}
