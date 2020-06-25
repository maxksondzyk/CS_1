package com.ksondzyk.storage;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
public class Product {

    @Getter @Setter
    private int amount;
    private String name;
    private int groupID;
    private int price;
    private int id;


    public String toString(){
        return name+": "+amount+";";
    }

}
