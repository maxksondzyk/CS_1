package com.ksondzyk.DataBase;


import com.ksondzyk.dao.Table;
import com.ksondzyk.utilities.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class TableTest {
    @Before
    public void init() {
        DB.connect();
        Table.deleteTable();
        Table.createTable();

    }


    @Test
    public void insert() throws SQLException {
        Table.insert( "fruits","apple", 100, 38);
        Table.insert( "fruits","apple", 0, 0);


        ResultSet apple = Table.selectOneByTitle("apple", Properties.tableName);

        assertEquals(100, apple.getInt("quantity"));
        assertEquals("apple", apple.getString("title"));

    }

    @Test
    public void update() throws SQLException {
        Table.insert( "fruits","cherry", 999, 25);
        Table.update("cherry",1000, 24);

        ResultSet cherry = Table.selectOneByTitle("cherry", Properties.tableName);

        assertEquals(1000, cherry.getInt("quantity"));
        assertEquals(24, cherry.getInt("price"));


    }
    @Test
    public void update2() throws SQLException {
        Table.insert( "fruits","cherry", 999, 25);
        Table.update("cherry",1000, "price");

        ResultSet cherry = Table.selectOneByTitle("cherry", Properties.tableName);

        assertEquals(1000, cherry.getInt("price"));


    }





    @Test
    public void delete() throws SQLException {
        Table.insert("pomidors", "small", 99129, 1);
        Table.delete("small");


        assert(Table.selectOneByTitle("small", Properties.tableName).isClosed());

    }



    @After
    public void close() {
        DB.close();
    }
}