package com.ksondzyk;

import com.ksondzyk.DataBase.DB;
import com.ksondzyk.Processing.ProcessingQueue;

import com.ksondzyk.network.TCP.TCPServerThread;
import com.ksondzyk.network.UDPClientThread;
import com.ksondzyk.network.UDPServerThread;
import com.ksondzyk.utilities.Properties;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    static int networkThreadCount = 5;
    static ExecutorService executorPool;
    public static int processingThreadCount = 5;
    enum ProcessingType { Queue, Async }
    static ProcessingType processingType = ProcessingType.Queue;
    public static Boolean serverIsWorking = true;
    public static int secondsPerTask = 2;
    static ServerSocket s;

    public static void main(String[] args) {
        try {
            DB.connect();
            s = new ServerSocket( Properties.PORT);

        if (processingType == ProcessingType.Queue) {
            ProcessingQueue.runProcessing();

            System.out.println("Using queue");
        } else {
            System.out.println("Using async");
        }
        executorPool = Executors.newFixedThreadPool(networkThreadCount);
        if(Properties.MODE.equals("TCP")) {
            while (serverIsWorking)
                try {
                    executorPool.execute(new TCPServerThread(s.accept()));
                } catch (Exception e) {
                    if (serverIsWorking)
                        e.printStackTrace();
                }
        }
        else{
            executorPool.execute(new UDPServerThread());
        }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}