package com.ksondzyk.network.TCPNetwork;

import lombok.Getter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Data {
    public static final int PORT = 5087;
    @Getter
    InetAddress addr;
    @Getter
    private ServerSocket s;

    Data(int host){
        try {
            addr = InetAddress.getByName(null);

            if(host==0)
            s = new ServerSocket(PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
