package com.ksondzyk.storage;

import com.ksondzyk.storage.ProductsGroup;
import lombok.Getter;

import java.util.ArrayList;

public class ProductsStorage {

    @Getter
    private ArrayList<ProductsGroup> groups;

    public ProductsStorage() {
        this.groups = new ArrayList<>();
    }
}
