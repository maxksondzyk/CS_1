package com.ksondzyk.network.TCPNetwork;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Data {
    @Getter
    InetAddress addr;
    @Getter
    private ServerSocket s;
    @Getter @Setter
    int state;
    Data(int host){
        try {
            addr = InetAddress.getByName(null);
            if(host==0)
            s = new ServerSocket(5087);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
