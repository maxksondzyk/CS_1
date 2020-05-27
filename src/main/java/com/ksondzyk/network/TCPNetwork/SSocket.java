package com.ksondzyk.network.TCPNetwork;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.ServerSocket;

public class SSocket {
    @Getter
    private ServerSocket s;
    @Getter @Setter
    int state;
    SSocket(){
        try {
            s = new ServerSocket(5087);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
