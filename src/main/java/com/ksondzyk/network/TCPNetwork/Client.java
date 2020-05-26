package com.ksondzyk.network.TCPNetwork;


import java.io.IOException;
import java.net.InetAddress;


public class Client {


        static final int MAX_THREADS = 5;

        public static void main(String[] args) throws IOException, InterruptedException {
            InetAddress addr = InetAddress.getByName(null);
            while (true) {
                if (ClientThread.getThreadcount() < MAX_THREADS)
                    new ClientThread(addr);
                Thread.currentThread().sleep(100);
            }
        }



}