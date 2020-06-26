/*
File: editGroupFrame.java
Author: Danylo Vanin
 */
package com.ksondzyk.gui_swing;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.network.TCP.TCPClientThread;
import com.ksondzyk.storage.ProductGroup;
import com.ksondzyk.utilities.CipherMy;
import org.json.JSONObject;

public class editGroupFrame extends javax.swing.JFrame {

    /**
     * Creates new form editGroupFrame
     */
    public editGroupFrame() {
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

        editGroupChooser = new javax.swing.JComboBox<>();
        editGroupButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        editGroupTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);
        setType(Type.POPUP);

        String[] temp = new String[Storage.productsGroups.size()];
        for(int i = 0;i<temp.length;i++){
            ProductGroup group = Storage.productsGroups.get(i);
            temp[i] = group.getName();
        }

        editGroupChooser.setModel(new javax.swing.DefaultComboBoxModel<>(temp));
        editGroupButton.setText("Змінити назву");
        editGroupButton.addActionListener(evt -> editGroupButtonActionPerformed());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editGroupChooser, 0, 200, 500)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(editGroupTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(editGroupButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editGroupChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(editGroupButton, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(editGroupTextField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    /**
     * Proceeds to editing the group
     */
    private void editGroupButtonActionPerformed() {
        // TODO add your handling code here:
        String groupToEdit = String.valueOf(editGroupChooser.getSelectedItem());
        String newName = editGroupTextField.getText();



        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cType","1");
        jsonObject.put("type","categoryTitle");
        jsonObject.put("title",groupToEdit);

        Packet packet = new Packet((byte) 1,new Message(1,1,jsonObject.toString(),false));
        TCPClientThread tcpClientThread = new TCPClientThread(packet);
        Packet answer = tcpClientThread.send();
        String jsonString = CipherMy.decode(answer.getBMsq().getMessage());

        JSONObject responseMessage = new JSONObject(jsonString);
        int id = responseMessage.getInt("id");

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id",id);
        jsonObject1.put("cType","3");
        jsonObject1.put("type","category");
        jsonObject1.put("title",newName);

        Packet packet1 = new Packet((byte) 1,new Message(1,1,jsonObject1.toString(),false));
        TCPClientThread tcpClientThread1 = new TCPClientThread(packet1);
        Packet answer1 = tcpClientThread1.send();

        for(ProductGroup p: Storage.productsGroups){
            if(p.getId()==id){
                p.setName(newName);
                break;
            }
        }
        //StorageFrame.editGroup(groupToEdit, newName);
        dispose();
    }


    // Variables declaration - do not modify
    private javax.swing.JButton editGroupButton;
    private javax.swing.JComboBox<String> editGroupChooser;
    private javax.swing.JTextField editGroupTextField;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration
}
