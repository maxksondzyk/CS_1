package com.ksondzyk;

import com.ksondzyk.DataBase.DB;
import com.ksondzyk.HTTP.dao.Table;
import com.ksondzyk.Processing.Processor;
import com.ksondzyk.network.TCP.TCPServerThread;
import com.ksondzyk.network.UDP.UDPServerThread;
import com.ksondzyk.utilities.Properties;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Server {

    static int networkThreadCount = 10;
    static ExecutorService executorPool;
    public static int processingThreadCount = 10;
    public static Boolean serverIsWorking = true;
    public static int secondsPerTask = 1;
    static ServerSocket s;

    public static void main(String[] args) {
        try {
            DB.connect();
            //Table.createTable();
            //Table.createCategoriesTable();
            //Table.createUsersTable();
            //Table.insertUser("user","pass");
            s = new ServerSocket( Properties.PORT);

        executorPool = Executors.newFixedThreadPool(networkThreadCount);
        if(Properties.MODE.equals("TCP")) {
            while (true)
                try {
                    executorPool.execute(new TCPServerThread(s.accept()));
                } catch (Exception e) {

                }
        }
        else{
            executorPool.execute(new UDPServerThread());
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        executorPool.shutdown();
        try {
            if (!executorPool.awaitTermination(60, TimeUnit.SECONDS))
                System.err.println("Network threads didn't finish in 60 seconds!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Processor.shutdown();

    }

}