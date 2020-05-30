package com.ksondzyk;

import com.ksondzyk.network.ClientThread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

        public static void main(String[] args) {
            try {
                InetAddress addr = InetAddress.getByName(null);
            ExecutorService service = Executors.newFixedThreadPool(7);
            for(int i = 0;i<5;i++){
                Socket socket = new Socket(addr, Server.PORT);
                    service.execute(new ClientThread(socket));
            }
            service.shutdown();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



}