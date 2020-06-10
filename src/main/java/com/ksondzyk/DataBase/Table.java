package com.ksondzyk.DataBase;


import com.ksondzyk.utilities.Properties;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Table {

    public static void createTable() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS "+ Properties.tableName +" (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	category text NOT NULL,\n"
                + "	title text NOT NULL,\n"
                + "	quantity INTEGER, \n"
                + " price INTEGER,\n"
                + " UNIQUE(title)"
                + ");";
        try {
            Statement statement = DB.connection.createStatement();

            statement.execute(sqlQuery);

            System.out.println("Table created");
            System.out.println();
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    public static void deleteTable() {

        try {
            Statement   stmt = DB.connection.createStatement();

        String sqlCommand = "DROP TABLE IF EXISTS "+Properties.tableName;

        System.out.println("output : " + stmt.executeUpdate(sqlCommand));

        stmt.close(); }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet selectOneByTitle(String title) {
        String sqlQuery = "SELECT * FROM " + Properties.tableName +  " WHERE title = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, title);

            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }
    public static ResultSet selectOneById(int id) {
        String sqlQuery = "SELECT * FROM " + Properties.tableName +  " WHERE id = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, id);

            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }
    public static ResultSet selectAll() {
        String sqlQuery = "SELECT * FROM " + Properties.tableName;

        try {
            Statement statement  = DB.connection.createStatement();

            return statement.executeQuery(sqlQuery);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static Integer insert(String category, String title, int quantity, int price) {
        String sqlQuery = "INSERT OR IGNORE INTO " + Properties.tableName
                +  " (category,title,quantity,price) VALUES (?,?, ?, ?)";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, category);
            preparedStatement.setString(2, title);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setInt(4, price);

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

    public static void update( int id, String title,String category , int price, int quantity ) {
        String sqlQuery = "UPDATE " + Properties.tableName
                + " SET title =?, category = ?, price =?, quantity = ? WHERE id = ? ";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(5, id);
            preparedStatement.setString(1, title);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setInt(3, price);
            preparedStatement.setString(2, category);


            preparedStatement.executeUpdate();

            System.out.println("Updated #" + id + " Title: " + title + " Quantity" + quantity + " Price:" + price + " into " + Properties.tableName);
            System.out.println();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    public static void update(String title,  int quantity,  int price ) {
        String sqlQuery = "UPDATE " + Properties.tableName + " SET quantity = ?, price =? WHERE title = ? ";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(3, title);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, price);



            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void update(String title,  int value, String mode) {
        String sqlQuery;

        if(mode.equals("quantity")) {
            sqlQuery = "UPDATE " + Properties.tableName + " SET quantity = ? WHERE title = ? ";
        }
        else {
            sqlQuery = "UPDATE " + Properties.tableName + " SET price = ? WHERE title = ? ";
        }

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(2, title);
            preparedStatement.setInt(1, value);

            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }



    public static ResultSet selectOneLimitOffset( int limit, int offset) {
        String sqlQuery = "SELECT * FROM " + Properties.tableName +  " LIMIT ?, ?";

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

    public static ResultSet listByPrice(boolean fromLower){
        String order;
        if(fromLower)
            order = "ASC";
        else
            order = "DESC";
        String sqlQuery = "SELECT * FROM " + Properties.tableName +  " ORDER BY price "+order;

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);


            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;

    }

    public static ResultSet listByPrice(boolean fromLowest, String category){

        String order = fromLowest? "ASC":"DESC";

        String sqlQuery = "SELECT * FROM " + Properties.tableName +  " WHERE category = ? ORDER BY price "+order;

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, category);


            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;



    }

    public static void delete(int id) {
        String sqlQuery = "DELETE FROM " + Properties.tableName + " WHERE id = ?";

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
    public static void delete(String title) {
        String sqlQuery = "DELETE FROM " + Properties.tableName + " WHERE title = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, title);

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + title);
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void truncate() {
        String sqlQuery = "DELETE FROM " + Properties.tableName;

        try {
            Statement statement = DB.connection.createStatement();

            statement.execute(sqlQuery);

            System.out.println("Table  truncated");
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}