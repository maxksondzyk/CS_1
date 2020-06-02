package com.ksondzyk;

import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.IOException;
/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

private String INPUT = "test";


    public void testCheckPacket() {
        try {
            Message testMessage = new Message(100, 7731265, INPUT,false);
            Packet p = new Packet((byte)10, testMessage);
            Packet pr = new Packet(p.getData());
            Assert.assertTrue(pr.checkCRC());
            Assert.assertEquals(INPUT, pr.getMessage());
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

}
