package com.ksondzyk;

import com.ksondzyk.DataBase.DB;
import com.ksondzyk.DataBase.Table;
import com.ksondzyk.Processing.ProcessingQueue;

import com.ksondzyk.network.TCP.TCPServerThread;
import com.ksondzyk.network.UDPServerThread;
import com.ksondzyk.utilities.NetworkProperties;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    static int networkThreadCount = 1;
    static ExecutorService executorPool;
    public static int processingThreadCount = 1;
    enum ProcessingType { Queue, Async }
    static ProcessingType processingType = ProcessingType.Queue;
    public static Boolean serverIsWorking = true;
    public static int secondsPerTask = 2;
    static ServerSocket s;

    public static void main(String[] args) {
        try {
            DB.connect();
            s = new ServerSocket( NetworkProperties.PORT);

        String mode = NetworkProperties.MODE;
        if (processingType == ProcessingType.Queue) {
            ProcessingQueue.runProcessing();

            System.out.println("Using queue");
        } else {
            System.out.println("Using async");
        }
        executorPool = Executors.newFixedThreadPool(networkThreadCount);
        while (serverIsWorking)
            try {
                executorPool.execute(new TCPServerThread(s.accept()));
            } catch (Exception e) {
                if (serverIsWorking)
                    e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}