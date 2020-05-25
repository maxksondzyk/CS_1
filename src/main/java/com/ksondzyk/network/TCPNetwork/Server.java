package com.ksondzyk.network.TCPNetwork;

import com.ksondzyk.network.Network;
import com.ksondzyk.network.TCPNetwork_еуые;
import com.ksondzyk.storage.Product;
import com.ksondzyk.storage.ProductsGroup;
import com.ksondzyk.storage.ProductsStorage;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    public static void main(String[] args) {
       try {
           ServerSocket serverSocket = new ServerSocket(5023);
           ExecutorService service = Executors.newFixedThreadPool(2);

           for(;; ){
               service.execute(new ServerThread(serverSocket.accept()));
           }

            /*  Network network = new TCPNetwork_еуые();

            network.listen();

            network.receive();

            network.receive();

            network.receive();

            network.close();  */
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

       }
    }
}