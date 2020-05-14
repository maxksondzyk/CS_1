package com.ksondzyk;

import org.json.simple.JSONObject;

import java.nio.ByteBuffer;

public class Message {
    private int cType;
    private int bUserId;
    private JSONObject message;
    public Message(int cType, int bUserId,String message){
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = new JSONObject();
        this.message.put("MESSAGE",message);
    }
    public byte[] toBytes(){
        byte[] res;
        byte[] bytes = message.toString().getBytes();
        ByteBuffer temp = ByteBuffer.allocate(8+bytes.length);
        temp.putInt(this.cType);
        temp.putInt(this.bUserId);
        temp.put(bytes);
        res = temp.array();
        return res;
    }
    public String getMessage(){
        return message.toString();
    }
}
