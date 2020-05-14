package com.ksondzyk;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class  PacketReceiver {
    public PacketReceiver(byte[] packet){
        System.out.println(checkCRC(packet));
    }

    private boolean checkCRC(byte[] packet) {
        int wLen = packet.length-Packet.B_MSQ-(Packet.B_MSQ-Packet.W_CRC_16);
        return((CRC.calculate_crc(Arrays.copyOfRange(packet,Packet.B_MAGIC,Packet.W_CRC_16))== ByteBuffer.wrap(Arrays.copyOfRange(packet,Packet.W_CRC_16,Packet.B_MSQ)).getShort())&&
                (CRC.calculate_crc(Arrays.copyOfRange(packet,Packet.B_MSQ,Packet.B_MSQ+wLen))== ByteBuffer.wrap(Arrays.copyOfRange(packet,Packet.B_MSQ+wLen,Packet.B_MSQ+wLen+(Packet.B_MSQ-Packet.W_CRC_16))).getShort()));
    }
}
