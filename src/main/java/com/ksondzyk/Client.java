package com.ksondzyk;

import com.ksondzyk.network.UDPClientThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

        public static void main(String[] args) {
            ExecutorService service = Executors.newFixedThreadPool(10);
            for(int i = 0;i<10;i++){
                service.execute(new UDPClientThread());
            }
            service.shutdown();
        }



}