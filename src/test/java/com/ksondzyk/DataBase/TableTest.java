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
    public void update3() throws SQLException {
        Table.insert( "fruits","cherry", 999, 25);
        Table.update(1,"cherryBoy","fruits",1000, 100);

        ResultSet cherry = Table.selectOneById(1,Properties.tableName);


        assertEquals("cherryBoy", cherry.getString("title"));


    }
@Test
    public void listBy() throws SQLException {
        Table.insert("fruits", "cherry", 1, 25);
        Table.insert("fruits", "strawberry", 1, 20);
        Table.insert("fruits", "pear", 1, 15);
        Table.insert("fruits", "pineapple", 1, 10);
        Table.insert("fruits", "banana", 1, 5);
        Table.insert("fruits", "apple", 1, 25);

        ResultSet result = Table.listBy("price",true);


    result.next();
        assertEquals("banana",result.getString(3));
        result.next();
    assertEquals("pineapple",result.getString(3));
    result.next();
    assertEquals("pear",result.getString(3));
    result.next();
    assertEquals("strawberry",result.getString(3));


    }

    @Test
    public void listByPrice() throws SQLException {
        Table.insert("berry", "cherry", 1, 25);
        Table.insert("berry", "strawberry", 1, 20);
        Table.insert("berry", "raspberry", 1, 15);
        Table.insert("fruits", "pineapple", 1, 10);
        Table.insert("fruits", "banana", 1, 5);
        Table.insert("fruits", "apple", 1, 25);

        ResultSet result = Table.listByPrice(false,"berry");


        result.next();
        assertEquals("cherry",result.getString(3));
        result.next();
        assertEquals("strawberry",result.getString(3));
        result.next();
        assertEquals("raspberry",result.getString(3));


    }

    @Test
    public void selectOneByTitle() throws SQLException {
        Table.insert( "fruits","cherry", 999, 25);
        ResultSet cherry = Table.selectOneByTitle("cherry", Properties.tableName);

        assertEquals("cherry", cherry.getString("title"));
        assertEquals("fruits", cherry.getString("category"));
        assertEquals(1, cherry.getInt("id"));
        assertEquals(999, cherry.getInt("quantity"));
        assertEquals(25, cherry.getInt("price"));

    }
    @Test
    public void selectOneByLimitOffset() throws SQLException {
        Table.insert("fruits", "cherry", 1, 25);
        Table.insert("fruits", "strawberry", 1, 20);
        Table.insert("fruits", "pear", 1, 15);
        Table.insert("fruits", "pineapple", 1, 10);
        Table.insert("fruits", "banana", 1, 5);
        Table.insert("fruits", "apple", 1, 25);


        ResultSet result = Table.selectOneLimitOffset(1,0);

        assertEquals(1,result.getInt(1));

        result = Table.selectOneLimitOffset(2,3);
        result.next();
        assertEquals(4,result.getInt(1));
        result.next();
        assertEquals(5,result.getInt(1));

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