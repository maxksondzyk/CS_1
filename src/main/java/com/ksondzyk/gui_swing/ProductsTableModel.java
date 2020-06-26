/*
File: ProductsTableModel.java
Author: Kate Bilorus
 */
package com.ksondzyk.gui_swing;

import com.ksondzyk.storage.Product;

import javax.swing.table.AbstractTableModel;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

public class ProductsTableModel extends AbstractTableModel {
    private List<Product> products;

    public ProductsTableModel(List<Product> products) {
        this.products = products;

    }

    /**
     * counts the rows
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return products.size();
    }

    /**
     * counts the cilumns
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return 5;
    }

    /**
     * gets the column name
     *
     * @param c
     * @return
     */
    @Override
    public String getColumnName(int c) {
        String result = "";
        switch (c) {
            case 0:
                result = "ID";
                break;
            case 1:
                result = "Name";
                break;
            case 2:
                result = "Group";
                break;
            case 3:
                result = "Quantity";
                break;
            case 4:
                result = "Price";
                break;
        }
        return result;
    }

    /**
     * gets the value of a particular tile
     *
     * @param r
     * @param c
     * @return
     */
    @Override
    public Object getValueAt(int r, int c) {
        switch (c) {
            case 0:
                return products.get(r).getId();
            case 1:
                return products.get(r).getName();
            case 2:
                return products.get(r).getGroupID();
            case 3:
                return products.get(r).getAmount();
            case 4:
                return products.get(r).getPrice();

            default:
                return "";
        }
    }

    /**
     * checks if the cell is editable
     *
     * @param row
     * @param column
     * @return
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 3&&column!=0;
    }
}