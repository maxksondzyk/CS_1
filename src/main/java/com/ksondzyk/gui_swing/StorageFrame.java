package com.ksondzyk.gui_swing;


import com.ksondzyk.storage.Product;
import com.ksondzyk.storage.ProductGroup;

import javax.swing.*;
import javax.swing.table.TableModel;

import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

/**
 * @author Danil
 */
public class StorageFrame extends javax.swing.JFrame {
    /**
     * Creates new form StorageFrame
     */
    StorageFrame() {
        super("Керування складом");
        initComponents();
        loadData();
        setComboBoxProductsGroup();
        java.awt.EventQueue.invokeLater(() -> setVisible(true));

    }

    /**
     * Deletes group
     * @param groupToRemove
     */
    public static void deleteGroup(String groupToRemove) {



        productsTable.revalidate();
        productsTable.repaint();
    }

    /**
     * Edits the group
     * @param groupToEdit
     * @param newName
     */
    public static void editGroup(String groupToEdit, String newName){
        Storage.productsGroups.remove(groupToEdit);
//        String desc = Storage.gr.get(groupToEdit).getDescription();
//        Storage.gr.remove(groupToEdit);
//        Storage.productsGroups.add(newName);
//        setComboBoxProductsGroup();
//        Storage.gr.put(newName, new ProductsGroup(newName, desc));
//        for (Product p:Storage.products) {
//            if (p.getGroup().getTitle().equals(groupToEdit)) p.getGroup().setTitle(newName);
//        }
        Storage.model.fireTableDataChanged();
        productsTable.revalidate();
        productsTable.repaint();
    }


    private void loadData()
    {

            Storage.download();
        productsTable.revalidate();
        productsTable.repaint();
    }

    /**
     * Recreates combobox
     */
    private static void setComboBoxProductsGroup()
    {
        comboBoxProductGroups.removeAllItems();
        for(ProductGroup g : Storage.productsGroups) {
            comboBoxProductGroups.addItem(g.getName());
        }

    }

    /**
     * Adds product group
     * @param productsGroup
     */
    public static void addProductGroup(String productsGroup)
    {
       // Storage.productsGroups.add(productsGroup );
        setComboBoxProductsGroup();
    }

    /**
     * Adds product to list
     * @param product
     */
    private static void addProductToList(Product product)

    {
        //Storage.products.add( product );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        searchField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        tablePanel = new javax.swing.JPanel();
        tableScrollPane = new javax.swing.JScrollPane();
        productsTable = new javax.swing.JTable(Storage.model);
        productManagePanel = new javax.swing.JPanel();
        addProductButton = new javax.swing.JButton();
        removeProductButton = new javax.swing.JButton();
        importProductButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        groupManagePanel = new javax.swing.JPanel();
        addGroupButton = new javax.swing.JButton();
        removeGroupButton = new javax.swing.JButton();
        editGroupButton = new javax.swing.JButton();
        showInfoButton = new javax.swing.JButton();

        /**
         * Closing window and saving file with storage info
         */
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);


        searchButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        searchButton.setText("Знайти");
        searchButton.addActionListener(evt -> searchButtonActionPerformed());

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
                searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(searchPanelLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        searchPanelLayout.setVerticalGroup(
                searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(searchField)
                                        .addComponent(searchButton))
                                .addGap(5, 5, 5))
        );


        /**
         * Table customization
         */

        tableScrollPane.setViewportView(productsTable);
        productsTable.getColumnModel().getColumn(0).setMinWidth(40);
        productsTable.getColumnModel().getColumn(1).setMinWidth(40);
        productsTable.getColumnModel().getColumn(1).setMaxWidth(120);
        productsTable.getColumnModel().getColumn(2).setMinWidth(210);
        productsTable.getColumnModel().getColumn(3).setMinWidth(100);
        productsTable.getColumnModel().getColumn(4).setMinWidth(60);
        productsTable.getColumnModel().getColumn(4).setMaxWidth(70);

        productsTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBoxProductGroups));

        javax.swing.GroupLayout tablePanelLayout = new javax.swing.GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(
                tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(tablePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(tableScrollPane)
                                .addContainerGap())
        );
        tablePanelLayout.setVerticalGroup(
                tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
        );

        addProductButton.setText("Додати товар");
        addProductButton.addActionListener(evt -> addProductButtonActionPerformed());

        removeProductButton.setText("Видалити товар");
        removeProductButton.addActionListener(evt -> removeProductButtonActionPerformed());

        importProductButton.setText("Прибуття/списання товару");
        importProductButton.addActionListener(evt -> importProductButtonActionPerformed());

        javax.swing.GroupLayout productManagePanelLayout = new javax.swing.GroupLayout(productManagePanel);
        productManagePanel.setLayout(productManagePanelLayout);
        productManagePanelLayout.setHorizontalGroup(
                productManagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(productManagePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(addProductButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(removeProductButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(importProductButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        productManagePanelLayout.setVerticalGroup(
                productManagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(productManagePanelLayout.createSequentialGroup()
                                .addGroup(productManagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(addProductButton)
                                        .addComponent(removeProductButton)
                                        .addComponent(importProductButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        addGroupButton.setText("Додати группу");
        addGroupButton.addActionListener(evt -> addGroupButtonActionPerformed());

        removeGroupButton.setText("Видалити группу");
        removeGroupButton.addActionListener(evt -> removeGroupButtonActionPerformed());

        editGroupButton.setText("Редагувати группу");
        editGroupButton.addActionListener(evt -> editGroupButtonActionPerformed());


        javax.swing.GroupLayout groupManagePanelLayout = new javax.swing.GroupLayout(groupManagePanel);
        groupManagePanel.setLayout(groupManagePanelLayout);
        groupManagePanelLayout.setHorizontalGroup(
                groupManagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(groupManagePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(addGroupButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(removeGroupButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(editGroupButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        groupManagePanelLayout.setVerticalGroup(
                groupManagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(groupManagePanelLayout.createSequentialGroup()
                                .addGroup(groupManagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(addGroupButton)
                                        .addComponent(removeGroupButton)
                                        .addComponent(editGroupButton))
                                .addContainerGap())
        );

        showInfoButton.setText("Показати інформацію");
        showInfoButton.addActionListener(evt -> showInfoButtonActionPerformed());

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(productManagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(groupManagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(showInfoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(productManagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(groupManagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(showInfoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        this.setSize(790,500);
    }

    /**
     * Makes search in table
     */
    private void searchButtonActionPerformed() {
        TableModel model = productsTable.getModel();
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        productsTable.setRowSorter(sorter);
        String text = searchField.getText();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(text));
        }

    }

    /**
     * proceeds to importing the product
     */
    private void importProductButtonActionPerformed() {

        new importProductFrame();
    }

    /**
     * proceeds to removing the product
     */
    private void removeProductButtonActionPerformed() {
        if (productsTable.getSelectedRows().length > 0) Storage.products.remove(productsTable.getSelectedRows()[0]);
        Storage.model.fireTableDataChanged();
    }

    /**
     * proceeds to adding the product
     */
    private void addProductButtonActionPerformed() {
        addProductToList( new Product());
        Storage.model.fireTableDataChanged();
        setComboBoxProductsGroup();
    }

    /**
     * proceeds to showing the information
     */
    private void showInfoButtonActionPerformed() {

     //   new showInfoFrame();
    }

    /**
     * proceeds to adding the group
     */
    private void addGroupButtonActionPerformed() {
        new addGroupFrame();
        Storage.model.fireTableDataChanged();
        productsTable.revalidate();
        productsTable.repaint();
    }

    /**
     * proceeds to removing the group
     */
    private void removeGroupButtonActionPerformed() {

        new removeGroupFrame();
    }

    /**
     * proceeds to editing the group
     */
    private void editGroupButtonActionPerformed() {

        new editGroupFrame();
    }

    // Variables declaration - do not modify
    private static JComboBox comboBoxProductGroups = new JComboBox();
    private javax.swing.JButton addGroupButton;
    private javax.swing.JButton addProductButton;
    private javax.swing.JButton editGroupButton;
    private javax.swing.JPanel groupManagePanel;
    private javax.swing.JButton importProductButton;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel productManagePanel;
    private static javax.swing.JTable productsTable;
    private javax.swing.JButton removeGroupButton;
    private javax.swing.JButton removeProductButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JButton showInfoButton;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JScrollPane tableScrollPane;
    // End of variables declaration
}
