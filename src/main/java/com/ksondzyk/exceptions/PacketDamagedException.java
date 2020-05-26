package com.ksondzyk.exceptions;

public class PacketDamagedException extends Exception {
    public PacketDamagedException(short expected, short real) {
        super("CRC16 in packet does not match: \nExpected: "+ expected+";\nReal: "+real);
    }
    public PacketDamagedException(String message) {
        super(message);
    }
}
