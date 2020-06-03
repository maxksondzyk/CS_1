package com.ksondzyk.utilities;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.exceptions.PacketDamagedException;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class PacketReceiver {



    public static Packet receiveUDP(DatagramSocket serverSocket) throws IOException {
        DatagramPacket receivedPacket;
        byte[] receivedData;

        InetAddress IPAddress=null;
        int port=0;

   try {
            receivedData = new byte[Packet.packetMaxSize];
            receivedPacket = new DatagramPacket(receivedData, receivedData.length);

            serverSocket.receive(receivedPacket);

            IPAddress = receivedPacket.getAddress();
            port = receivedPacket.getPort();

            ByteBuffer byteBuffer = ByteBuffer.wrap(receivedData);
            Integer wLen = byteBuffer.getInt(Packet.packetPartFirstLengthWithoutwLen);

            byte[] fullPacket = new byte[Packet.packetPartFirstLength + Message.BYTES_WITHOUT_MESSAGE + wLen];
            byteBuffer.get(fullPacket);

            Packet result = new Packet(fullPacket);

            if(result.getBSrc()==(byte)1) {
                Packet packet = PacketGenerator.allOkPacket(result.getBPktId());
                DatagramPacket sendPacket = new DatagramPacket(packet.getData(), packet.getData().length, IPAddress, port);

                serverSocket.send(sendPacket);
            }
            return result;

    } catch (PacketDamagedException e) {
       Packet packet = PacketGenerator.errorPacket();
       DatagramPacket sendPacket = new DatagramPacket(packet.getData(), packet.getData().length, IPAddress, port);
       serverSocket.send(sendPacket);

   }
   return PacketGenerator.errorPacket();
    }

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

        Packet packet = new Packet(fullPacket);

        System.err.println(CipherMy.decode(packet.getBMsq().getMessage()));



        return packet;

}catch(PacketDamagedException e){
            PacketGenerator packetGenerator = new PacketGenerator();
            System.err.println("The packet has not been fully sent");
                return (packetGenerator.newPacket(1, "END"));
}

    }

}



