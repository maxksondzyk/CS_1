package com.ksondzyk.network.TCPNetwork;

import com.ksondzyk.network.Network;
import com.ksondzyk.network.TCPNetwork_еуые;
import com.ksondzyk.storage.Product;
import com.ksondzyk.storage.ProductsGroup;
import com.ksondzyk.storage.ProductsStorage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    public static final int PORT = 5087;

    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Сервер запущено.");

        try {
            while (true) {
                Socket socket = s.accept();
                    try {
                    new ServerThread(socket);
                } catch (IOException e) {
                    //в разі невдачі закриваємо сокет
                    socket.close();
                }
            }
        } finally {
            s.close();
        }
    }

}