package com.ksondzyk.DataBase;

import com.ksondzyk.utilities.NetworkProperties;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Table {
    public static void create(String name) {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + name + " (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, quantity INTEGER, price INTEGER)";

        try {
            Statement statement = DB.connection.createStatement();

            statement.execute(sqlQuery);

            System.out.println("Table " + name + " created");
            System.out.println();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static Integer insert(String tableName, String title, int quantity, int price) {
        String sqlQuery = "INSERT INTO " + tableName +  " (title) VALUES (?, ?, price)";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, quantity);
            preparedStatement.setInt(3, price);

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                Integer id = resultSet.getInt(1);

                System.out.println("Inserted " + id + " " + title);
                System.out.println();

                return id;
            } else {
                System.err.println("Can't insert :(");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static void insert(String tableName, int id, int quantity, int price, String title) {
        String sqlQuery = "INSERT INTO " + tableName +  " (id, title, quantity, price) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setInt(4, price);

            preparedStatement.executeUpdate();

            System.out.println("Inserted #" + id + " Title: " + title + " Quantity" + quantity + " Price:" + price + " into " + tableName);
            System.out.println();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void update(String tableName, int id, int quantity, String title, int price) {
        String sqlQuery = "UPDATE " + tableName + " SET title = ? quantity = ? price = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setInt(4, price);

            preparedStatement.executeUpdate();

            System.out.println("Updated #" + id + " Title: " + title + " Quantity" + quantity + " Price:" + price + " into " + tableName);
            System.out.println();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static ResultSet selectOneByTitle(String tableName, String title) {
        String sqlQuery = "SELECT * FROM " + tableName +  " WHERE title = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, title);

            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static ResultSet selectOneLimitOffset(String tableName, int limit, int offset) {
        String sqlQuery = "SELECT * FROM " + tableName +  " LIMIT ?, ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, limit);

            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }


    public static ResultSet selectAll(String tableName) {
        String sqlQuery = "SELECT * FROM " + tableName;

        try {
            Statement statement  = DB.connection.createStatement();

            return statement.executeQuery(sqlQuery);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static void delete(String tableName, int id) {
        String sqlQuery = "DELETE FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + id);
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void truncate(String tableName) {
        String sqlQuery = "DELETE FROM " + tableName;

        try {
            Statement statement = DB.connection.createStatement();

            statement.execute(sqlQuery);

            System.out.println("Table " + tableName + " truncated");
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}