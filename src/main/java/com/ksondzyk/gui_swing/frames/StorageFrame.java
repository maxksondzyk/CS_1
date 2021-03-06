package com.ksondzyk.gui_swing.frames;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.gui_swing.ProductsTableModel;
import com.ksondzyk.gui_swing.Storage;
import com.ksondzyk.gui_swing.addGroupFrame;
import com.ksondzyk.network.TCP.TCPClientThread;
import com.ksondzyk.storage.Product;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;

public class StorageFrame extends javax.swing.JFrame {

    public static ArrayList<Product> products = new ArrayList<>();
    public static HashSet<String> productsGroups = new HashSet<>();
    private static ProductsTableModel model = new ProductsTableModel( products );

    /**
     * Creates new form StorageFrame
     */
    public StorageFrame() {
        super("Керування складом");
        initComponents();
        Storage.download();
        setComboBoxProductsGroup();

        productsTable.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                TableCellEditor tce = productsTable.getCellEditor();
                if(tce != null)
                    tce.stopCellEditing();
            }
        });
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });


    }

    /**
     * Deletes group
     * @param groupToRemove
     */
    public static void deleteGroup(String groupToRemove) {
        productsGroups.remove(groupToRemove);
        setComboBoxProductsGroup();


       // productsTable.revalidate();
        //productsTable.repaint();
    }


    /**
     * Adds group name to combo box chooser in table
     * @param groupName
     */
    public static void addComboBoxProductsGroup(String groupName) {
        comboBoxProductGroups.addItem(groupName);
    }

    /**
     * Recreates combobox
     */
    private static void setComboBoxProductsGroup()
    {
        comboBoxProductGroups.removeAllItems();
        for(String temp : productsGroups) {
            comboBoxProductGroups.addItem(temp);
        }

    }

    /**
     * Fills product @productGroups with names of group from file
     * @param productGroupNames
     */
    public static void fillProductsGroupsHashSet(HashSet<String> productGroupNames) {
        productsGroups=productGroupNames;
    }

    /**
     * Adds product group
     * @param productsGroup
     */
    public static void addProductGroup(String productsGroup)
    {
        productsGroups.add( productsGroup );
    }

    /**
     * Adds product to list
     * @param product
     */
    public static void addProductToList(Product product )
    {
        products.add( product );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        searchField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        tablePanel = new javax.swing.JPanel();
        tableScrollPane = new javax.swing.JScrollPane();
        productsTable = new javax.swing.JTable(model);
        productManagePanel = new javax.swing.JPanel();
        addProductButton = new javax.swing.JButton();
        removeProductButton = new javax.swing.JButton();
        importProductButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        groupManagePanel = new javax.swing.JPanel();
        addGroupButton = new javax.swing.JButton();
        removeGroupButton = new javax.swing.JButton();
        editGroupButton = new javax.swing.JButton();
        editProductButton = new javax.swing.JButton();
        showInfoButton = new javax.swing.JButton();

        /**
         * Closing window and saving file with storage info
         */
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //Files.write( products );
                mainPanel.setVisible(false);
                dispose();
            }
        });

        searchButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        searchButton.setText("Find");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

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
        //Column "Назва"
        productsTable.getColumnModel().getColumn(0).setMinWidth(120);
        //Column "К-сть"
        productsTable.getColumnModel().getColumn(1).setMinWidth(40);
        productsTable.getColumnModel().getColumn(1).setMaxWidth(50);
        //Column "Група товарів"
        productsTable.getColumnModel().getColumn(2).setMinWidth(210);
        //Column "Опис"
        productsTable.getColumnModel().getColumn(3).setMinWidth(100);
        //Column "Ціна"
        productsTable.getColumnModel().getColumn(4).setMinWidth(60);
        productsTable.getColumnModel().getColumn(4).setMaxWidth(70);




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

        addProductButton.setText("Add product");
        addProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductButtonActionPerformed(evt);
            }
        });

        removeProductButton.setText("Remove product");
        removeProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeProductButtonActionPerformed(evt);
            }
        });

        importProductButton.setText("Change the amount");
        importProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importProductButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout productManagePanelLayout = new javax.swing.GroupLayout(productManagePanel);
        productManagePanel.setLayout(productManagePanelLayout);
        productManagePanelLayout.setHorizontalGroup(
                productManagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(productManagePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(addProductButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                              //  .addComponent(editProductButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                      //  .addComponent(editProductButton)
                                        .addComponent(removeProductButton)
                                        .addComponent(importProductButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        addGroupButton.setText("Add category");
        addGroupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGroupButtonActionPerformed(evt);
            }
        });

        removeGroupButton.setText("Remove category");
        removeGroupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeGroupButtonActionPerformed(evt);
            }
        });

        editGroupButton.setText("Edit category");
        editGroupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editGroupButtonActionPerformed(evt);
            }
        });
        editProductButton.setText("Edit product");
        editProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProductButtonActionPerformed(evt);
            }
        });


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

        showInfoButton.setText("Show info");
        showInfoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showInfoButtonActionPerformed(evt);
            }
        });

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
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Makes search in table
     * @param evt
     */
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        TableModel model = productsTable.getModel();
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        productsTable.setRowSorter(sorter);
        String text = searchField.getText();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(text));
        }

    }//GEN-LAST:event_searchButtonActionPerformed

    private void importProductButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importProductButtonActionPerformed
        // TODO add your handling code here:
        new importProductFrame();

    }//GEN-LAST:event_importProductButtonActionPerformed

    private void removeProductButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeProductButtonActionPerformed


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cType","4");
        jsonObject.put("type","good");
        jsonObject.put("id",products.get(productsTable.getSelectedRows()[0]).getId());
        Packet packet = new Packet((byte) 1,new Message(1,1,jsonObject.toString(),false));
        TCPClientThread tcpClientThread = new TCPClientThread(packet);
        Packet answer = tcpClientThread.send();



        if (productsTable.getSelectedRows().length > 0) products.remove(productsTable.getSelectedRows()[0]);
        model.fireTableDataChanged();
    }//GEN-LAST:event_removeProductButtonActionPerformed

    private void addProductButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductButtonActionPerformed

        //addProductToList( new Product() );
        new AddProductFrame();
        model.fireTableDataChanged();
    }//GEN-LAST:event_addProductButtonActionPerformed

    private void showInfoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showInfoButtonActionPerformed
        new showInfoFrame();
    }//GEN-LAST:event_showInfoButtonActionPerformed

    private void addGroupButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new addGroupFrame();
    }

    private void removeGroupButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new removeGroupFrame();
    }

    private void editGroupButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new editGroupFrame();
    }
    private void editProductButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new editProductFrame();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static JComboBox comboBoxProductGroups = new JComboBox();
    private javax.swing.JButton addGroupButton;
    private javax.swing.JButton addProductButton;
    private javax.swing.JButton editGroupButton;
    private javax.swing.JButton editProductButton;
    private javax.swing.JPanel groupManagePanel;
    private javax.swing.JButton importProductButton;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel productManagePanel;
    static javax.swing.JTable productsTable;
    private javax.swing.JButton removeGroupButton;
    private javax.swing.JButton removeProductButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JButton showInfoButton;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JScrollPane tableScrollPane;
    // End of variables declaration//GEN-END:variables

}
