package com.ksondzyk;

import com.ksondzyk.network.Network;
import com.ksondzyk.network.TCPNetwork;


public class Server {
    public static void main(String[] args) {
        try {
            Network network = new TCPNetwork();

            network.listen();

            network.receive();

            network.receive();

            network.receive();

            network.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}