package com.ksondzyk.network.TCPNetwork;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

        public static void main(String[] args) {
            Data data = new Data(1);
            ExecutorService service = Executors.newFixedThreadPool(7);
            for(int i = 0;i<5;i++){
                    service.execute(new ClientThread(data));
            }
            service.shutdown();
        }



}