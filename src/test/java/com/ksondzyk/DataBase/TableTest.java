package com.ksondzyk.DataBase;


import com.ksondzyk.HTTP.dao.Table;
import com.ksondzyk.utilities.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
    public void create() throws SQLException {
        Table.createTable();
        ResultSet results = Table.selectAll();
        ResultSetMetaData md = results.getMetaData();
        int columnCount = md.getColumnCount();



        for (int i = 1; i <= columnCount; i++) {

            String columnName = md.getColumnName(i);
            if (i == 1)
                assertEquals("id", columnName);

            else if (i == 2)
                assertEquals("category", columnName);

            else if (i == 3)
            assertEquals("title", columnName);

            else if (i == 4)
                assertEquals("quantity", columnName);

            else
                assertEquals("price", columnName);


        }


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

    @Test
    public void truncate() throws SQLException {
        Table.insert("fruits", "cherry", 1, 25);
        Table.insert("fruits", "strawberry", 1, 9);
        Table.insert("fruits", "pear", 1, 9);

        Table.truncate();
        assert(Table.selectOneByTitle("cherry", Properties.tableName).isClosed());
        assert(Table.selectOneByTitle("pear", Properties.tableName).isClosed());
        assert (Table.selectOneById(1,Properties.tableName).isClosed());

    }

    @After
    public void close() {
        DB.close();
    }
}