package com.ksondzyk.network.TCPNetwork;

import com.ksondzyk.network.Network;
import com.ksondzyk.network.TCPNetwork_еуые;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    public static void main(String[] args) {
       try {
            ServerSocket serverSocket = new ServerSocket(5023);
           ExecutorService service = Executors.newFixedThreadPool(8);

           while(true){
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
        }
    }
}