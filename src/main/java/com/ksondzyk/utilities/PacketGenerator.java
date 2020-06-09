package com.ksondzyk.utilities;

import com.google.common.primitives.UnsignedLong;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;

import java.util.Random;

public class PacketGenerator {

    //answers from server
        public static Packet errorPacket(){
            return new Packet((byte)0, new Message(-1,-1,"send again",false));
    }
    //answers from server
        public static Packet allOkPacket(UnsignedLong bPktID){
        return new Packet((byte)0,bPktID,new Message(1,1,"all ok",false));
            }


            private UnsignedLong bPktID = UnsignedLong.ZERO;

    public Packet newPacket(int clientID, String message){
        bPktID = bPktID.plus(UnsignedLong.ONE);
        Random rand = new Random();


        return  new Packet((byte)1,bPktID,
                new Message(rand.nextInt(5)+1, clientID, message,false));

    }
    public Packet newPacket(int i){
        bPktID = bPktID.plus(UnsignedLong.ONE);
        Random rand = new Random();
        String message ="fruits,apples,123,10";

//        if (i == 1) {
//            return new Packet((byte)1,bPktID,
//                    new Message(rand.nextInt(5) + 1,
//                            19,
//                            message, false));
//        }
                if (i == 0) {
            return new Packet((byte)1,bPktID,
                    new Message(4,
                            1,
                            message, false));
        }
        if (i == 1) {
            message = "fruits,apples,23,10";
            return new Packet((byte)1,bPktID,
                    new Message(2,
                            1,
                            message, false));
        }
        if (i == 2) {
            message = "fruits,apples,123,15";
            return new Packet((byte)1,bPktID,
                    new Message(5,
                            1,
                            message, false));
        }
        if (i == 3) {
            message = "fruits,apples,123,15";
            return new Packet((byte)1,bPktID,
                    new Message(1,
                            1,
                            message, false));
        }
        return new Packet((byte) 1,bPktID,
                new Message(rand.nextInt(5) + 1,
                        rand.nextInt(),
                        message, false));


    }
}
