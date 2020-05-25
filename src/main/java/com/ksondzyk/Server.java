package com.ksondzyk;

<<<<<<< HEAD
public class Server {
}
=======
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
>>>>>>> ba09185eb547c565fd32ce0c7b3b523cfe8a18ee
