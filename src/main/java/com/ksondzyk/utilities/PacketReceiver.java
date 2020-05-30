package com.ksondzyk.utilities;

import com.github.snksoft.crc.CRC;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;

import com.ksondzyk.exceptions.PacketDamagedException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class PacketReceiver {


   public Packet receive(InputStream serverInputStream) throws IOException, PacketDamagedException {


           byte[] maxPacketBuffer = new byte[Packet.packetMaxSize];

           serverInputStream.read(maxPacketBuffer);

           ByteBuffer byteBuffer = ByteBuffer.wrap(maxPacketBuffer);
           Integer wLen = byteBuffer.getInt(Packet.packetPartFirstLengthWithoutwLen);

           byte[] fullPacket = new byte[Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen];
           byteBuffer.get(fullPacket);
           System.out.println("Received");
           System.out.println(Arrays.toString(fullPacket) + "\n");

           Packet packet = new Packet(fullPacket);
           System.err.println(packet.getBMsq().getMessage());

       return packet;

   }

}


