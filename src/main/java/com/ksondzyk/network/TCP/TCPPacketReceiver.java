package com.ksondzyk.network.TCP;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.exceptions.PacketDamagedException;
import com.ksondzyk.utilities.CipherMy;
import com.ksondzyk.utilities.PacketGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class TCPPacketReceiver {

    public Packet receive(InputStream serverInputStream) throws IOException {

        try {

        byte[] maxPacketBuffer = new byte[Packet.packetMaxSize];
        while (serverInputStream.read()!=Packet.bMagic);
        serverInputStream.read(maxPacketBuffer);

        ByteBuffer byteBuffer = ByteBuffer.wrap(maxPacketBuffer);
        Integer wLen = byteBuffer.getInt(Packet.packetPartFirstLengthWithoutwLen-1);

        byte[] fullPacket = new byte[Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen];
        byteBuffer.get(fullPacket);
        System.out.println("Received");
        System.out.println(Arrays.toString(fullPacket) + "\n");

        Packet packet = new Packet(fullPacket,"TCP");

        System.err.println(CipherMy.decode(packet.getBMsq().getMessage()));



        return packet;

}catch(PacketDamagedException e){
            PacketGenerator packetGenerator = new PacketGenerator();
            System.err.println("The packet has not been fully sent");
                return (packetGenerator.newPacket(1, "END"));
}

    }

}



