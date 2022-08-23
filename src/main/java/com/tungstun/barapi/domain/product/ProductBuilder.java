package com.tungstun.barapi.domain.product;

import com.tungstun.common.money.Money;

public class ProductBuilder {
    private final Long barId;
    private final String name;
    private final Category category;
    private String brand = "";
    private double size = 0;
    private boolean isFavorite = false;
    private Money price = new Money(0.0);
    private ProductType type = ProductType.OTHER;

    public ProductBuilder(Long barId, String name, Category category) {
        this.barId = barId;
        this.name = name;
        this.category = category;
    }

    public ProductBuilder setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public ProductBuilder setSize(double size) {
        this.size = size;
        return this;
    }

    public ProductBuilder setFavorite(boolean favorite) {
        isFavorite = favorite;
        return this;
    }

    public ProductBuilder setPrice(Double price) {
        this.price = new Money(price);
        return this;
    }

    public ProductBuilder setType(ProductType type) {
        this.type = type;
        return this;
    }

    public Product build() {
        return new Product(
                this.barId,
                this.name,
                this.brand,
                this.size,
                this.isFavorite,
                this.type,
                this.price,
                this.category);
    }
}
