package com.ksondzyk;

import com.ksondzyk.network.TCPNetwork.Client;
import com.ksondzyk.network.TCPNetwork.Server;
import junit.framework.TestCase;

public class ThreadsTest extends TestCase {
    public void testExchange(){
     Server.start();
     Client.start();

    }

}
