package com.tungstun.barapi.domain.product;

public class ProductBuilder {
    private String name;
    private String brand;
    private double size;
    private double price;
    private boolean isFavorite;
    private Category category;

    public ProductBuilder() {
        this.name = "Unknown";
        this.brand = "Unknown";
        this.size = 0;
        this.price = 0.0;
        this.isFavorite = false;
        this.category = null;
    }

    public ProductBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public ProductBuilder setSize(double size) {
        this.size = size;
        return this;
    }

    public ProductBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    public ProductBuilder setFavorite(boolean favorite) {
        isFavorite = favorite;
        return this;
    }

    public ProductBuilder setCategory(Category category) {
        this.category = category;
        return this;
    }

    public Product build(){
        return new Product(
                this.name,
                this.brand,
                this.size,
                this.price,
                this.isFavorite,
                this.category);
    }
}
