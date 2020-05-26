package com.ksondzyk.network.TCPNetwork;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


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