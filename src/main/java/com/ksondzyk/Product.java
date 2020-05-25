package com.ksondzyk;

import lombok.Getter;
import lombok.Setter;

public class Product {
    @Getter @Setter
    private int amount;

    @Getter @Setter
    private String name;

    public Product(int amount, String name){
        this.amount = amount;
        this.name = name;
    }

    public String toString(){
        return name+": "+amount+";";
    }

}
