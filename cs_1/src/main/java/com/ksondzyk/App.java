package com.ksondzyk;

import lombok.var;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public App() throws IOException {
    }

    public static void main(String[] args) throws IOException {
            if (args.length != 1) {
                System.err.println("Pass the server IP as the sole command line argument");
                return;
            }
            var socket = new Socket(args[0], 59090);
            var in = new Scanner(socket.getInputStream());
            System.out.println("Server response: " + in.nextLine());

            Packet packet = new Packet((byte) 1,100,2456,"hello");
            PacketReceiver packetReceiver = new PacketReceiver(packet.getData());
            System.out.println("Message: "+packetReceiver.getMessage()+"\nPacket is intact: "+packetReceiver.checkCRC());

    }
}
