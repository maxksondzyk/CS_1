package com.ksondzyk;

import com.ksondzyk.utilities.CipherMy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CipherMyTest {

    String testString;
    @Before
    public void setUp(){
        testString="Successful test";
    }

    @Test
    public void encode() {
        String encoded = CipherMy.encode(testString);
        Assert.assertNotEquals(testString, encoded);
        Assert.assertEquals(testString, CipherMy.decode(encoded));
    }

    @Test
    public void decode() {
        String encoded = CipherMy.encode(testString);
        Assert.assertEquals(testString, CipherMy.decode(encoded));
    }
}