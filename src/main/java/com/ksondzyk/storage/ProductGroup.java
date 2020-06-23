package com.ksondzyk.storage;

import lombok.Data;


import java.util.ArrayList;
@Data
public class ProductGroup {

    private ArrayList<Product> products;
    private String name;
    private int id;

}
