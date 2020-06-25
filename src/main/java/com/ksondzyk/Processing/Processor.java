package com.ksondzyk.Processing;

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

    private static Message answer(int cType,JSONObject jsonObject) throws PacketDamagedException {
        answerMessage = new JSONObject();
        try {
            String category;
            String title;
            int id;
            int quantity;
            int price;

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

                String type = (String) jsonObject.get("type");
               switch (type) {
                   case("good"):
                       id = Integer.parseInt(String.valueOf(jsonObject.get("id")));
                       if (!idPresent(id, Properties.tableName)) {
                           answerMessage.put("status", "not");
                       } else {
                           Product product = Table.selectProductById(id);
                           answerMessage.put("id", product.getId());
                           answerMessage.put("title", product.getName());
                           answerMessage.put("quantity", product.getAmount());
                           answerMessage.put("price", product.getPrice());
                           answerMessage.put("categoryId", product.getGroupID());
                           answerMessage.put("status", "ok");

                       }
                       break;
                   case (("user")) :
                       String password = Table.selectOneByTitle("user", "Users").getString("password");
                       answerMessage.put("password", password);

                       break;

                       case("allGoods"):
                       ArrayList<Product> goods = (ArrayList<Product>) Table.selectAllProducts();
                       JSONArray array = new JSONArray(goods);
                       answerMessage.put("goods", array);
                       answerMessage.put("status", "ok");

                       break;
                   case("categories"):
                       ArrayList<ProductGroup> groups = (ArrayList<ProductGroup>) Table.selectAllGroups();
                       array = new JSONArray((groups));
                       answerMessage.put("groups", array);
                       answerMessage.put("status", "ok");
                       break;
                   case("categoryProducts"):
                       id = Integer.parseInt(String.valueOf(jsonObject.get("id")));
                       if (!idPresent(id, Properties.tableCategories)) {
                           answerMessage.put("status", "not");
                       } else {
                           ArrayList<Product> products = (ArrayList<Product>) Table.selectAllProducts(id);
                           array = new JSONArray((products));
                           answerMessage.put("products", array);
                           answerMessage.put("status", "ok");
                       }
                       break;
                   case("info"):
                       int value;
                       String id1 = String.valueOf(jsonObject.get("id"));
                       if(id1.equals("all")) {
                           value = Table.getValue();
                       } else{
                           value = Table.getValue(Integer.parseInt(id1));
                       }
                       answerMessage.put("value",value);
                   break;
                       default:
                       id = Integer.parseInt(String.valueOf(jsonObject.get("id")));
                       if (!idPresent(id, "Categories")) {
                           answerMessage.put("status", "not");
                       } else {
                           title = Table.selectOneById(id, "Categories").getString("title");
                           answerMessage.put("id", id);
                           answerMessage.put("title", title);
                           answerMessage.put("status", "ok");
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
                int categoryId = 0;
                id = Integer.parseInt(String.valueOf(jsonObject.get("id")));
                if(jsonObject.get("type").equals("good")) {
                    if(!idPresent(id,Properties.tableName)) {
                        answerMessage.put("status","not");
                    }else {
                    if (jsonObject.has("category")) {
                        category = (String) jsonObject.get("category");
                    } else {
                        categoryId = Table.selectOneById(id, Properties.tableName).getInt("categoryId");
                        category = Table.selectOneById(categoryId, "Categories").getString("title");
                    }
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


    @Override
    public Message call()  {
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
