package com.ksondzyk.network.TCPNetwork;

import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Data {
    @Getter
    InetAddress addr;
    @Getter @Setter
    int state;
    Data(){
        try {
            addr = InetAddress.getByName(null);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
