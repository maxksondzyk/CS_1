
package com.ksondzyk.gui_swing.frames;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.gui_swing.Storage;
import com.ksondzyk.network.TCP.TCPClientThread;
import com.ksondzyk.storage.Product;
import com.ksondzyk.utilities.CipherMy;
import org.json.JSONObject;

import javax.swing.*;

public class editProductFrame extends javax.swing.JFrame {

    /**
     * Creates new form editGroupFrame
     */
    public editProductFrame() {
        super("Редагувати группу");
        initComponents();
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        productChooser = new JComboBox<>();
        createGroupButton = new javax.swing.JButton();
        titleField = new javax.swing.JTextField();
        categoryField = new javax.swing.JTextField();
        categoryLabel = new javax.swing.JLabel();
        quantityLabel = new javax.swing.JLabel();
        priceLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        quantityField = new JSpinner();
        priceField = new JSpinner();

        createGroupButton.setText("Edit");
        createGroupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProductButtonActionPerformed();
            }
        });

        titleLabel.setText("Title");
        priceLabel.setText("Price");
        categoryLabel.setText("Category");
        quantityLabel.setText("Quantity");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);
        setType(Type.POPUP);
        String[] temp = new String[Storage.products.size()];
        for (int i = 0; i < temp.length; i++){
            temp[i] = Storage.products.get(i).getName();
        }
        productChooser.setModel(new DefaultComboBoxModel<>(temp));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup()
                                //  .addContainerGap(64, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(productChooser, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(categoryField, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(quantityField, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(62, 62, 62))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(titleLabel)
                                        .addGap(164, 164, 164))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(createGroupButton, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(89, 89, 89))
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(categoryLabel)
                                        .addGap(159, 159, 159)).addGroup(layout.createSequentialGroup()
                                        .addComponent(priceLabel)
                                        .addGap(164, 164, 164)).addGroup(layout.createSequentialGroup()
                                        .addComponent(quantityLabel)
                                        .addGap(164, 164, 164))
                        ));

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(productChooser, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                // .addContainerGap(29, Short.MAX_VALUE)
                                .addComponent(categoryLabel)
                                //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                //.addGap(18, 18, 18)
                                .addComponent(titleLabel)
                                .addGap(5, 5, 5)
                                .addComponent(categoryField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(priceLabel)
                                //  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(quantityLabel)
                                // .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(quantityField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(createGroupButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))
        );

        pack();
    }

    /**
     * Proceeds to editing the product
     */
    private void editProductButtonActionPerformed() {
        String product = String.valueOf(productChooser.getSelectedItem());
        String title = titleField.getText();
        String category = categoryField.getText();
        int quantity = -1;
        int price = -1;
        if(quantityField.getValue()!="")
            quantity = (int) quantityField.getValue();
        if(priceField.getValue()!="")
            price = (int) priceField.getValue();

        int id = 0;
        for (Product p : Storage.products) {
            if (p.getName().equals(product)){
                id = p.getId();
            }
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cType","3");
        jsonObject.put("type","good");
        jsonObject.put("id",id);
        if(category!="")
        jsonObject.put("category",category);
        if(title!="")
        jsonObject.put("title",title);

        if(price>=0)
        jsonObject.put("price",price);
        if(quantity>=0)
        jsonObject.put("quantity",quantity);



        Packet packet = new Packet((byte) 1,new Message(1,1,jsonObject.toString(),false));
        TCPClientThread tcpClientThread = new TCPClientThread(packet);
        Packet answer = tcpClientThread.send();
        String jsonString = CipherMy.decode(answer.getBMsq().getMessage());

        JSONObject responseMessage = new JSONObject(jsonString);

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("cType","1");
        jsonObject1.put("type","good");
        jsonObject1.put("id",id);

        Packet packet1 = new Packet((byte) 1,new Message(1,1,jsonObject1.toString(),false));
        TCPClientThread tcpClientThread1 = new TCPClientThread(packet1);
        Packet answer1 = tcpClientThread1.send();
        String jsonString1 = CipherMy.decode(answer1.getBMsq().getMessage());

        JSONObject responseMessage1 = new JSONObject(jsonString1);

        for(Product p: Storage.products){
            if(p.getId()==id){
                p.setName(responseMessage1.getString("title"));
                p.setAmount(responseMessage1.getInt("quantity"));
                p.setPrice(responseMessage1.getInt("price"));
                p.setGroupID(responseMessage1.getInt("categoryId"));
                break;
            }
        }
        //StorageFrame.editGroup(groupToEdit, newName);
        StorageFrame.productsTable.revalidate();
        StorageFrame.productsTable.repaint();
        Storage.model.fireTableDataChanged();
        dispose();
    }


    // Variables declaration - do not modify
    private javax.swing.JButton createGroupButton;
    private javax.swing.JTextField titleField;
    private javax.swing.JTextField categoryField;
    private javax.swing.JSpinner quantityField;
    private javax.swing.JSpinner priceField;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JLabel categoryLabel;
    private javax.swing.JLabel titleLabel;
    private JComboBox<String> productChooser;
    // End of variables declaration
}
