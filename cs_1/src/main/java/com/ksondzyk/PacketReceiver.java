package com.ksondzyk;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class PacketReceiver {
    public PacketReceiver(byte[] packet){
        System.out.println(checkCRC(packet));
    }

    private boolean checkCRC(byte[] packet) {
        return(CRC.calculate_crc(Arrays.copyOfRange(packet,0,14))== ByteBuffer.wrap(Arrays.copyOfRange(packet,14,18)).getInt());
    }
}
