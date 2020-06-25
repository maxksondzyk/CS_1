package com.ksondzyk.Processing;

import com.google.gson.Gson;
import com.ksondzyk.HTTP.dao.Table;
import com.ksondzyk.Server;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.exceptions.PacketDamagedException;
import com.ksondzyk.storage.Product;
import com.ksondzyk.storage.ProductGroup;
import com.ksondzyk.utilities.CipherMy;
import com.ksondzyk.utilities.Properties;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.*;


public class Processor implements Callable{
    Packet packet;
    static OutputStream os;

    public Processor(Packet packet){
        this.packet = packet;
        run();
    }

    static ExecutorService executorPool = Executors.newFixedThreadPool(Server.processingThreadCount);

    static JSONObject answerMessage;
    static Message answer;

    public static boolean idPresent(int id, String tableName){
        try {
            Table.selectOneById(id,tableName).getInt("id");
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }
    public static boolean titlePresent(String title, String tableName){
        try {
            Table.selectOneByTitle(title,tableName).getString("title");
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

    private static Message answer(int cType,JSONObject jsonObject) throws PacketDamagedException {
        answerMessage = new JSONObject();
        try {
            String category;
            String title;
            int id;
            int quantity;
            int price;
            String type;
        switch (cType) {
            case 0:
                try {
                    String login = Table.selectOneByTitle("admin","Users").getString("title");
                    String password = Table.selectOneByTitle("admin","Users").getString("password");
                    if(login.equals(jsonObject.getString("login"))&&password.equals(jsonObject.getString("password"))){
                        answerMessage.put("status","ok");
                    }
                    else {
                        answerMessage.put("status", "not");
                    }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
                break;
            case 1:

                type = (String) jsonObject.get("type");
                if(type.equals("good")) {
                    id = Integer.parseInt(String.valueOf(jsonObject.get("id")));
                    if(!idPresent(id,Properties.tableName)) {
                        answerMessage.put("status","not");
                    }else {
                        title = Table.selectOneById(id, Properties.tableName).getString("title");
                        quantity = Table.selectOneById(id, Properties.tableName).getInt("quantity");
                        price = Table.selectOneById(id, Properties.tableName).getInt("price");
                        int categoryId = Table.selectOneById(id, Properties.tableName).getInt("categoryID");
                        category = Table.selectOneById(categoryId, "Categories").getString("title");
                        answerMessage.put("id", id);
                        answerMessage.put("title", title);
                        answerMessage.put("quantity", quantity);
                        answerMessage.put("price", price);
                        answerMessage.put("category", category);
                        answerMessage.put("categoryId", categoryId);
                        answerMessage.put("status","ok");
                    }
                }
                else if(type.equals("user")){
                   // String login = Table.selectOneByTitle((String) jsonObject.get("login"),"Users").getString("title");
                    String password = Table.selectOneByTitle("user","Users").getString("password");
                    answerMessage.put("password",password);
                }
                else if(type.equals("allGoods")){
                    ArrayList<Product> goods = (ArrayList<Product>) Table.selectAllProducts();
                   JSONArray array =new JSONArray (goods);
                   answerMessage.put("goods",array);
                    answerMessage.put("status","ok");

                }
                else if(type.equals("categories")){
                    ArrayList<ProductGroup> groups = (ArrayList<ProductGroup>) Table.selectAllGroups();
                    JSONArray array =new JSONArray (new Gson().toJson(groups));
                    answerMessage.put("groups",(Object)array);
                    answerMessage.put("status","ok");

                }
                else if(type.equals("categoryProducts")){
                    id = Integer.parseInt(String.valueOf(jsonObject.get("id")));
                    if(!idPresent(id,Properties.tableCategories)) {
                        answerMessage.put("status","not");
                    }else {
                        ArrayList<Product> groups = (ArrayList<Product>) Table.selectAllProducts(id);
                        JSONArray array = new JSONArray(new Gson().toJson(groups));
                        answerMessage.put("products", (Object) array);
                        answerMessage.put("status", "ok");
                    }
                }
                else if (type.equals("goodTitle")){
                    title = String.valueOf(jsonObject.get("id"));
                    if(!titlePresent(title,Properties.tableName)){
                        answerMessage.put("status","not");
                    }else {
                        Product product = Table.selectProductByTitle(title);
                        int categoryId = Table.selectOneByTitle(title, Properties.tableName).getInt("categoryID");
                        answerMessage.put("id", product.getId());
                        answerMessage.put("title", title);
                        answerMessage.put("quantity", product.getAmount());
                        answerMessage.put("price", product.getPrice());
                        answerMessage.put("category", product.getGroup());
                        answerMessage.put("categoryId", categoryId);
                        answerMessage.put("status","ok");
                    }

                }
                else if(type.equals("info")){
                    int value;
                    String id1 = String.valueOf(jsonObject.get("id"));
                    if(id1.equals("all")) {
                       value = Table.getValue();
                    } else{
                        value = Table.getValue(Integer.parseInt(id1));
                    }
                    answerMessage.put("value",value);
                }

                else {
                    id = Integer.parseInt(String.valueOf(jsonObject.get("id")));
                    if(!idPresent(id,"Categories")) {
                        answerMessage.put("status","not");
                    }else {
                        title = Table.selectOneById(id, "Categories").getString("title");
                        answerMessage.put("id", id);
                        answerMessage.put("title", title);
                        answerMessage.put("status","ok");
                    }
                }

                break;

            case 2:
                type = (String) jsonObject.get("type");
                if(type.equals("good")) {
                    category = (String) jsonObject.get("category");
                    title = (String) jsonObject.get("title");
                    price = Integer.parseInt(String.valueOf(jsonObject.get("price")));
                    quantity = Integer.parseInt(String.valueOf(jsonObject.get("quantity")));

                    id = Table.insert(category, title, quantity, price);
                    answerMessage.put("id",id);
                }
                else if(type.equals("category")){
                    title = (String) jsonObject.get("title");
                    id = Table.insertCategory(title);
                    answerMessage.put("id",id);
                }
                else{
                    Table.insertUser(jsonObject.getString("login"),jsonObject.getString("password"), jsonObject.getString("token"));
                }

                break;

            case 3:
                id = Integer.parseInt(String.valueOf(jsonObject.get("id")));
                if(jsonObject.get("type").equals("good")) {
                    if(!idPresent(id,Properties.tableName)) {
                        answerMessage.put("status","not");
                    }else {
                    if (jsonObject.has("category")) {
                        category = (String) jsonObject.get("category");
                    } else
                        category = Table.selectOneById(id, Properties.tableName).getString("category");

                    if (jsonObject.has("title")) {
                        title = (String) jsonObject.get("title");
                    } else
                        title = Table.selectOneById(id, Properties.tableName).getString("title");

                    if (jsonObject.has("price")) {
                        price = Integer.parseInt(String.valueOf(jsonObject.get("price")));
                    } else
                        price = Table.selectOneById(id, Properties.tableName).getInt("price");

                    if (jsonObject.has("quantity")) {
                        quantity = Integer.parseInt(String.valueOf(jsonObject.get("quantity")));
                    } else
                        quantity = Table.selectOneById(id, Properties.tableName).getInt("quantity");

                    Table.update(id, title, category, price, quantity);
                    answerMessage.put("status","ok");
                }}
                else {
                    if(!idPresent(id,"Categories")) {
                        answerMessage.put("status","not");
                    }else {
                        title = (String) jsonObject.get("title");
                        Table.updateCategory(id, title);
                        answerMessage.put("status","ok");
                    }
                }

                break;
            case 4:
                type = (String) jsonObject.get("type");

                id = Integer.parseInt(String.valueOf(jsonObject.get("id")));

                if(type.equals("good")) {
                    if(idPresent(id,Properties.tableName)) {
                        Table.delete(id);
                        answerMessage.put("status","ok");
                    }
                    else{
                        answerMessage.put("status","not");
                    }
                }
                else {
                    if(idPresent(id,"Categories")) {
                        Table.deleteCategory(id);
                        answerMessage.put("status","ok");
                    }
                    else{
                        answerMessage.put("status","not");
                    }
                }
                break;

            default:
                throw new PacketDamagedException("Unknown command");

        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        answer = new Message(1,1,answerMessage.toString(),false);
        return answer;
    }
    public static Future<Message> process(Packet packet, OutputStream ostream) {
        os = ostream;
        Callable<Message> processingAsync = new Processor(packet);
        return executorPool.submit(processingAsync);
    }
    public static Future<Message> process(JSONObject jsonObject) {
        Packet packet = new Packet(jsonObject);
        Callable<Message> processingAsync = new Processor(packet);
        return executorPool.submit(processingAsync);
    }
    public void run(){
        synchronized (packet) {
        String jsonString = CipherMy.decode(packet.getBMsq().getMessage());

           JSONObject jsonObject = new JSONObject(jsonString);
            int cType = Integer.parseInt(String.valueOf(jsonObject.get("cType")));
            try {
            answer = answer(cType,jsonObject);

            } catch (PacketDamagedException e) {
                e.printStackTrace();
            }
        }
    }
//    private static String process(Packet packet, OutputStream os) throws PacketDamagedException {
//
//        return CipherMy.decode(answerMessage.getMessage());
//    }

    @Override
    public Message call() throws Exception {
        try {
            Thread.sleep(50 * Server.secondsPerTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return answer;
    }
    public static void shutdown() {
        executorPool.shutdown();
        try {
            if (!executorPool.awaitTermination(60, TimeUnit.SECONDS))
                System.err.println("ProcessingAsync threads didn't finish in 60 seconds!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
