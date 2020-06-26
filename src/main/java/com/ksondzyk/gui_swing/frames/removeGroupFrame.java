package com.ksondzyk.gui_swing.frames;

//import static com.company.StorageFrame.productsGroups;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.gui_swing.Storage;
import com.ksondzyk.network.TCP.TCPClientThread;
import com.ksondzyk.storage.ProductGroup;
import com.ksondzyk.utilities.CipherMy;
import org.json.JSONObject;

public class removeGroupFrame extends javax.swing.JFrame {

    /**
     * Creates new form removeGroupFrame
     */
    public removeGroupFrame() {
        super("Видалити группу");
        initComponents();
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    //// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        deleteGroupChooser = new javax.swing.JComboBox<>();
        deleteGroupButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setAlwaysOnTop(true);
        setType(Type.POPUP);
        String[] temp = new String[Storage.productsGroups.size()];
        for(int i = 0;i<temp.length;i++){
            ProductGroup group = Storage.productsGroups.get(i);
            temp[i] = group.getName();
        }

        deleteGroupChooser.setModel(new javax.swing.DefaultComboBoxModel<>(temp));

        deleteGroupButton.setText("Delete");
        deleteGroupButton.addActionListener(evt -> deleteGroupButtonActionPerformed(evt));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(deleteGroupChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteGroupButton, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteGroupButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteGroupChooser))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * proceeds to deleting the group
     * @param evt
     */
    private void deleteGroupButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String groupToRemove = String.valueOf(deleteGroupChooser.getSelectedItem());
        StorageFrame.deleteGroup(groupToRemove);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cType","1");
        jsonObject.put("type","categoryTitle");
        jsonObject.put("title",groupToRemove);

        Packet packet = new Packet((byte) 1,new Message(1,1,jsonObject.toString(),false));
        TCPClientThread tcpClientThread = new TCPClientThread(packet);
        Packet answer = tcpClientThread.send();
        String jsonString = CipherMy.decode(answer.getBMsq().getMessage());
        JSONObject responseMessage = new JSONObject(jsonString);
        int id = responseMessage.getInt("id");

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("cType","4");
        jsonObject1.put("type","category");
        jsonObject1.put("id",id);

        Packet packet1 = new Packet((byte) 1,new Message(1,1,jsonObject1.toString(),false));
        TCPClientThread tcpClientThread1 = new TCPClientThread(packet1);
        Packet answer1 = tcpClientThread1.send();
        String jsonString1 = CipherMy.decode(answer1.getBMsq().getMessage());
        JSONObject responseMessage1 = new JSONObject(jsonString1);
        Storage.products.removeIf(p -> p.getGroupID() == id);
        Storage.productsGroups.removeIf(p -> p.getId() == id);
        StorageFrame.products.removeIf(p -> p.getGroupID() == id);
        StorageFrame.productsTable.revalidate();
        StorageFrame.productsTable.repaint();
        dispose();
      //  Storage.gr.remove(groupToRemove);

       // dispose();
        }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteGroupButton;
    private javax.swing.JComboBox<String> deleteGroupChooser;
    // End of variables declaration//GEN-END:variables
}
