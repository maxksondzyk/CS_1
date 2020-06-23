package com.ksondzyk.storage;

import lombok.Getter;

import java.util.ArrayList;

public class ProductsStorage {

    @Getter
    private ArrayList<ProductGroup> groups;

    public ProductsStorage() {
        this.groups = new ArrayList<>();
    }
}
