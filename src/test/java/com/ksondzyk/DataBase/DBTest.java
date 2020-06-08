package com.ksondzyk.DataBase;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class DBTest {


    @Test
    public void connect() throws SQLException {
    DB.connect();
    assert(DB.connection.isValid(10));
    DB.close();
    assert(DB.connection.isClosed());
    }

    @Test
    public void close() throws SQLException {
        DB.connect();
        assert(!DB.connection.isClosed());
        DB.close();
        assert(DB.connection.isClosed());
    }
}