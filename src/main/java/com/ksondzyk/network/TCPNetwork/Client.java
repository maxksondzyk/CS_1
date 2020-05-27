package com.ksondzyk.network.TCPNetwork;


import com.ksondzyk.network.TCPNetwork_еуые;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

        public static void main(String[] args) throws IOException, InterruptedException {
            Data data = new Data(1);
            ExecutorService service = Executors.newFixedThreadPool(7);
            for(int i = 0;i<5;i++){//while (true) {
                    service.execute(new ClientThread(data));
                Thread.currentThread().sleep(100);
            }
            service.shutdown();
        }



}