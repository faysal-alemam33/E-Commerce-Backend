package com.springboot.ecommerce.dto;

import com.springboot.ecommerce.entity.UnitType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;


@Data
public class ProductRequest {


    private String name;
    private UnitType unit;
    private BigDecimal price;
    private String description;
    private Set<Long> categoryIds;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
