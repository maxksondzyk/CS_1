package com.ksondzyk.network.TCPNetwork;


import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {


        static final int MAX_THREADS = 2;

        public static void main(String[] args) throws IOException, InterruptedException {
            InetAddress addr = InetAddress.getByName(null);
            ExecutorService service = Executors.newFixedThreadPool(7);
            while (true) {
                if (ClientThread.getThreadcount() < MAX_THREADS)
                    service.execute(new ClientThread(addr));
                Thread.currentThread().sleep(100);
            }
        }



}