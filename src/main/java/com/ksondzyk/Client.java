package com.ksondzyk;

//import com.ksondzyk.network.TCPClientThread;
import com.ksondzyk.network.UDPClientThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

        public static void main(String[] args) {
            String mode = "UDP";
            ExecutorService service = Executors.newFixedThreadPool(5);
            for(int i = 0;i<5;i++){
                service.execute(new UDPClientThread());
            }
            service.shutdown();
        }



}