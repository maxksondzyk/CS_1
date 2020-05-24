package com.ksondzyk;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class ProductsGroup {

    @Getter
    private ArrayList<Product> products;
    @Getter @Setter
    private String name;

    public ProductsGroup(String name)
    {
        this.name = name;
        products = new ArrayList<>();
    }
    public void addProducts(Product newProduct){
        for (Product product : products) {
            if (product.getName().equals(newProduct.getName())) {
                product.setAmount(product.getAmount() + newProduct.getAmount());
                return;
            }
        }
        products.add(newProduct);
    }
    public void deleteProducts(Product productToDelete){
        for (Product product : products) {
            if (product.getName().equals(productToDelete.getName())) {
                if (product.getAmount() < productToDelete.getAmount()) {
                    throw new IllegalArgumentException();
                }
                product.setAmount(product.getAmount() - productToDelete.getAmount());
                return;
            }
        }
    }
}
