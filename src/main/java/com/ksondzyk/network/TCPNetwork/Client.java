package com.ksondzyk.network.TCPNetwork;


import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

        public static void main(String[] args) throws IOException, InterruptedException {
            InetAddress addr = InetAddress.getByName(null);
            ExecutorService service = Executors.newFixedThreadPool(7);
            for(int i = 0;i<5;i++){//while (true) {
                    service.execute(new ClientThread(addr));
                Thread.currentThread().sleep(100);
            }
            service.shutdown();
        }



}