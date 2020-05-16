package com.ksondzyk;

import org.json.JSONObject;
import lombok.Data;
import java.nio.ByteBuffer;

@Data
public class Message {
    public static final int C_TYPE_OFFSET = 0;
    public static final int B_USER_ID_OFFSET = 4;
    public static final int MESSAGE_OFFSET = 8;
    private final int cType;
    private final int bUserId;
    private final JSONObject message;

    public Message(int cType, int bUserId, String message) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = new JSONObject();
        message = message.toLowerCase();
        message = CipherXOR.encode(message);
        this.message.put("MESSAGE", message);
    }

    public byte[] toBytes() {
        byte[] res;
        byte[] bytes = message.toString().getBytes();
        ByteBuffer temp = ByteBuffer.allocate(8 + bytes.length);
        temp.putInt(cType);
        temp.putInt(bUserId);
        temp.put(bytes);
        res = temp.array();
        return res;
    }
}
