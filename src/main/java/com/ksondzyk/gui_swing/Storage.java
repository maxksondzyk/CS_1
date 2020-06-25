
package com.ksondzyk.gui_swing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.network.TCP.TCPClientThread;
import com.ksondzyk.storage.Product;
import com.ksondzyk.storage.ProductGroup;
import com.ksondzyk.utilities.CipherMy;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Storage {
    public static ArrayList<Product> products = new ArrayList<>();
    public static HashSet<String> productsGroups = new HashSet<>();
    public static HashMap<String, ProductGroup> gr = new HashMap<>();
    public static ProductsTableModel model = new ProductsTableModel(products);
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

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

    /**
     * uploads everything to the file
     */
    static void upload(){
        String si = gson.toJson(products);
        File output = new File("Data.json");
        FileWriter fw;
        try {
            fw = new FileWriter(output);
            fw.write(si);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        si = gson.toJson(gr);
        output = new File("Groups.json");
        try {
            fw = new FileWriter(output);
            fw.write(si);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    static void download(){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cType","1");
        jsonObject.put("type","allGoods");


        Packet packet = new Packet((byte) 1,new Message(1,1,jsonObject.toString(),false));
        TCPClientThread tcpClientThread = new TCPClientThread(packet);
        Packet answer = tcpClientThread.send();

        String jsonString = CipherMy.decode(answer.getBMsq().getMessage());

        JSONObject responseMessage = new JSONObject(jsonString);
        if(responseMessage.get("status").equals("ok")) {
            System.out.println(responseMessage.toMap());

        }
        else{
            //setStatusCode(404);
        }
        Type collectionType = new TypeToken<ArrayList<Product>>(){}.getType();
        //ArrayList<Product> productsTemp = responseMessage.toMap();
        //for (Product p : productsTemp) {
        //    products.add(p);
        //}
        Storage.model.fireTableDataChanged();
//        si = "";
//        try {
//            fr = new FileReader("Groups.json");
//            BufferedReader bf = new BufferedReader(fr);
//            while((line = bf.readLine()) != null) {
//                si += line;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        collectionType = new TypeToken<HashMap<String, ProductGroup>>(){}.getType();
//        gr = gson.fromJson(si, collectionType);
//        for (Map.Entry<String, ProductGroup> g: gr.entrySet()){
//            productsGroups.add(g.getKey());
//        }
//        Storage.model.fireTableDataChanged();
//        try {
//            fr.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
