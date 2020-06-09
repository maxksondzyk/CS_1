package com.ksondzyk.DataBase;


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
                assertEquals("title", columnName);

            else if (i == 3)
                assertEquals("quantity", columnName);

            else
                assertEquals("price", columnName);


        }


    }

    @Test
    public void insert() {
        Table.insert(Properties.tableName, "apple", 100, 38);
    }

    @Test
    public void insert1() {
    }

    @Test
    public void update() {
    }

    @Test
    public void selectOneByTitle() {
    }

    @Test
    public void selectOneLimitOffset() {
    }

    @Test
    public void selectAll() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void truncate() {
    }

    @After
    public void close() {
        DB.close();
    }
}