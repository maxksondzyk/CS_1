package com.ksondzyk.DataBase;

import com.ksondzyk.utilities.NetworkProperties;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Table {
    public static void create() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + NetworkProperties.tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT)";

        try {
            Statement statement = DB.connection.createStatement();

            statement.execute(sqlQuery);

            System.out.println("Table " + NetworkProperties.tableName + " created");
            System.out.println();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static Integer insert(String title) {
        String sqlQuery = "INSERT INTO " + NetworkProperties.tableName +  " (title) VALUES (?)";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, title);

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

    public static void insert(int id, String title) {
        String sqlQuery = "INSERT INTO " + NetworkProperties.tableName +  " (id, title) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, title);

            preparedStatement.executeUpdate();

            System.out.println("Inserted " + id + " " + title);
            System.out.println();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void update(int id, String title) {
        String sqlQuery = "UPDATE " + NetworkProperties.tableName + " SET title = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

            System.out.println("Updated " + id + " " + title);
            System.out.println();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static ResultSet selectOneByTitle(String title) {
        String sqlQuery = "SELECT * FROM " + NetworkProperties.tableName +  " WHERE title = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, title);

            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static ResultSet selectOneLimitOffset(int limit, int offset) {
        String sqlQuery = "SELECT * FROM " + NetworkProperties.tableName +  " LIMIT ?, ?";

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


    public static ResultSet selectAll() {
        String sqlQuery = "SELECT * FROM " + NetworkProperties.tableName;

        try {
            Statement statement  = DB.connection.createStatement();

            return statement.executeQuery(sqlQuery);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static void delete(int id) {
        String sqlQuery = "DELETE FROM " + NetworkProperties.tableName + " WHERE id = ?";

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

    public static void truncate() {
        String sqlQuery = "DELETE FROM " + NetworkProperties.tableName;

        try {
            Statement statement = DB.connection.createStatement();

            statement.execute(sqlQuery);

            System.out.println("Table " + NetworkProperties.tableName + " truncated");
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}