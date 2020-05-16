package com.ksondzyk;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class  PacketReceiver {
    byte[] packet;
    public PacketReceiver(byte[] packet){
        this.packet = packet;
    }

    public boolean checkCRC() {
        int wLen = packet.length-Packet.B_MSQ_OFFSET -(Packet.B_MSQ_OFFSET -Packet.W_CRC_16_OFFSET);
        return(((short)CRC.calculateCRC(CRC.Parameters.CRC16,Arrays.copyOfRange(packet,Packet.B_MAGIC_OFFSET,Packet.W_CRC_16_OFFSET))== ByteBuffer.wrap(Arrays.copyOfRange(packet,Packet.W_CRC_16_OFFSET,Packet.B_MSQ_OFFSET)).getShort())&&
                ((short)CRC.calculateCRC(CRC.Parameters.CRC16,Arrays.copyOfRange(packet,Packet.B_MSQ_OFFSET,Packet.B_MSQ_OFFSET +wLen))== ByteBuffer.wrap(Arrays.copyOfRange(packet,Packet.B_MSQ_OFFSET +wLen,Packet.B_MSQ_OFFSET +wLen+(Packet.B_MSQ_OFFSET -Packet.W_CRC_16_OFFSET))).getShort()));
    }
    public String getMessage(){
        byte[] message = Arrays.copyOfRange(packet,Packet.B_MSQ_OFFSET +Message.MESSAGE_OFFSET +12,packet.length-2-(Packet.B_MSQ_OFFSET - Packet.W_CRC_16_OFFSET));
        String msg = new String(message);
        msg = CipherXOR.decode(msg);
        return msg;
    }
}
