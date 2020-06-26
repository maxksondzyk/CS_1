
package com.ksondzyk.gui_swing;


import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.gui_swing.frames.StorageFrame;
import com.ksondzyk.network.TCP.TCPClientThread;
import com.ksondzyk.storage.Product;
import com.ksondzyk.storage.ProductGroup;
import com.ksondzyk.utilities.CipherMy;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.*;

public class Storage {
    public static ArrayList<Product> products = new ArrayList<>();
    public static List<ProductGroup> productsGroups = new ArrayList<>();
    public static ProductsTableModel model = new ProductsTableModel(products);

    /**
     * checks if the product is unique
     * @param name
     * @return
     */
    static boolean checkForUniqueProduct(String name){
        for (Product p: products) {
            if (p.getName().equals(name)) return false;
        }
        return true;
    }




    public static void download(){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cType","1");
        jsonObject.put("type","allGoods");

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("cType","1");
        jsonObject2.put("type","categories");



        Packet packet = new Packet((byte) 1,new Message(1,1,jsonObject.toString(),false));
        TCPClientThread tcpClientThread = new TCPClientThread(packet);
        Packet answer = tcpClientThread.send();

        Packet packet2 = new Packet((byte) 1,new Message(1,1,jsonObject2.toString(),false));
        TCPClientThread tcpClientThread2 = new TCPClientThread(packet2);
        Packet answer2 = tcpClientThread2.send();

        String jsonString = CipherMy.decode(answer.getBMsq().getMessage());

        String jsonString2 = CipherMy.decode(answer2.getBMsq().getMessage());


        JSONObject responseMessage = new JSONObject(jsonString);
        if(responseMessage.get("status").equals("ok")) {
           productsFromJson(responseMessage);
        }
        JSONObject responseMessage2 = new JSONObject(jsonString2);
        if(responseMessage2.get("status").equals("ok")) {
            productsGroups = groupsFromJson(responseMessage2);
        }
        model= new ProductsTableModel(products);

        Storage.model.fireTableDataChanged();

    }

    private static List<ProductGroup> groupsFromJson(JSONObject jsn) {
        List<ProductGroup> listdata = new ArrayList<>();
        JSONArray jArray = jsn.getJSONArray("groups");
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                JSONObject o = jArray.getJSONObject(i);
                listdata.add(new ProductGroup(o.getInt("id"), o.getString("name")));
            }
        }
        return listdata;
    }

    private static ArrayList<Product> productsFromJson(JSONObject jsn){
        List<Product> listdata = new ArrayList<>();
        JSONArray jArray = jsn.getJSONArray("goods");
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                Product p = getProductFromJson(jArray.getJSONObject(i));
                StorageFrame.products.add(p);
                Storage.products.add(p);
            }
        }
        return (ArrayList<Product>) listdata;
    }

    private static Product getProductFromJson(JSONObject o) {
Product p = new Product();
p.setId(o.getInt("id"));
p.setName(o.getString("name"));
p.setAmount(o.getInt("amount"));
p.setPrice(o.getInt("price"));
p.setGroupID(o.getInt("groupID"));
return p;
    }
}
