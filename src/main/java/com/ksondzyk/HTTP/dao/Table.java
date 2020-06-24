package com.ksondzyk.HTTP.dao;

import com.ksondzyk.DataBase.DB;
import com.ksondzyk.storage.Product;
import com.ksondzyk.utilities.Properties;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Table {

    public static void createTable() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS "+ Properties.tableName +" (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	categoryID integer NOT NULL,\n"
                + "	title text NOT NULL,\n"
                + "	quantity INTEGER, \n"
                + " price INTEGER,\n"
                + " UNIQUE(title),\n"
                + " FOREIGN KEY(categoryID) REFERENCES Categories (id) ON DELETE CASCADE"
                + ");";

        String sqlQueryCategories = "CREATE TABLE IF NOT EXISTS "+ Properties.tableCategories +" (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	title text NOT NULL,\n"
                + " UNIQUE(title)"
                + ");";
        try {
            Statement statement = DB.connection.createStatement();

            statement.execute(sqlQuery);
            statement.execute(sqlQueryCategories);

            System.out.println("Table created\n");
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static void deleteTable() {

        try {
            Statement   stmt = DB.connection.createStatement();

        //String sqlCommand = "DROP TABLE IF EXISTS "+Properties.tableName;
            String sqlCommand = "DROP TABLE IF EXISTS "+"Users";

        System.out.println("output : " + stmt.executeUpdate(sqlCommand));

        stmt.close(); }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createUsersTable(){

        String sqlQueryUsers = "CREATE TABLE IF NOT EXISTS "+ "Users" +" (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	title text NOT NULL,\n"
                + "	password text, \n"
                + " token text, \n"
                + " UNIQUE(title)"
                + ");";
        try {
            Statement statement = DB.connection.createStatement();
            statement.execute(sqlQueryUsers);
            System.out.println("Table created\n");
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    public static Integer insertUser(String login, String password, String token) {
        try {
            String sqlQuery = "INSERT OR IGNORE INTO " + "Users"
                    +  " (title, password, token) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, token);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                Integer id = resultSet.getInt(1);

                System.out.println("Inserted User id:" + id + " " + login);
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

    public static Product selectProductByTitle(String title) {
        String sqlQuery = "SELECT * FROM " + Properties.tableName +  " WHERE title = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, title);
            ResultSet rs =  preparedStatement.executeQuery();


            return extractProduct(rs);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static ResultSet selectOneByTitle(String title, String table) {
        String sqlQuery = "SELECT * FROM " + table +  " WHERE title = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, title);
            return preparedStatement.executeQuery();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }
    public static Product selectProductById(int id, String table) {
        String sqlQuery = "SELECT * FROM " + table +  " WHERE id = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, id);

            return extractProduct(preparedStatement.executeQuery());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static ResultSet selectOneById(int id, String table) {
        String sqlQuery = "SELECT * FROM " + table +  " WHERE id = ?";

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

    public static Integer insert(String category, String title, int quantity, int price) {
        String sqlQuery = "INSERT OR IGNORE INTO " + Properties.tableName
                +  " (categoryID,title,quantity,price) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement;
        try {

                insertCategory(category);


            int categoryID = Table.selectOneByTitle(category,"Categories").getInt("id");
            preparedStatement = DB.connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, categoryID);
            preparedStatement.setString(2, title);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setInt(4, price);

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                Integer id = resultSet.getInt(1);

                System.out.println("Inserted id:" + id + " " + title);
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

    public static Integer insertCategory(String title) {
        try {
            String sqlCatQuery = "INSERT OR IGNORE INTO " + "Categories"
                    +  " (title) VALUES (?)";
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlCatQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                Integer id = resultSet.getInt(1);

                System.out.println("Inserted Category id:" + id + " " + title);
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
                + " SET title =?, categoryID = ?, price =?, quantity = ? WHERE id = ? ";

        try {
            int categoryID = Table.selectOneByTitle(category,"Categories").getInt(1);
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, categoryID);
            preparedStatement.setString(2, title);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setInt(4, price);


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

    public static void updateCategory( int id, String title) {
        String sqlQuery = "UPDATE " + "Categories"
                + " SET title = ? WHERE id = ? ";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

            System.out.println("Updated #" + id + " Title: " + title +" into Categories");
            System.out.println();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }


    public static List<Product> selectOneLimitOffsetProducts( int limit, int offset) {
        String sqlQuery = "SELECT * FROM " + Properties.tableName +  " LIMIT ?, ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, limit);


            List<Product> products = new ArrayList<>();
            ResultSet rs =  preparedStatement.executeQuery();

            while(rs.next()){
                products.add(extractProduct(rs));
            }

            return products;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
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
    public static List<Product> listProductsBy(String prop, boolean fromLower){
        String order;
        if(fromLower)
            order = "ASC";
        else
            order = "DESC";
        String sqlQuery = "SELECT * FROM " + Properties.tableName +  " ORDER BY "+prop+" "+order;

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            List<Product> products = new ArrayList<>();
            ResultSet rs =  preparedStatement.executeQuery();

            while(rs.next()){
                products.add(extractProduct(rs));
            }

            return products;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;

    }


    public static ResultSet listBy(String prop, boolean fromLower){
        String order;
        if(fromLower)
            order = "ASC";
        else
            order = "DESC";
        String sqlQuery = "SELECT * FROM " + Properties.tableName +  " ORDER BY "+prop+" "+order;

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);


            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;

    }
    public static List<Product> listProductsByPrice(boolean fromLowest, String category){

        String order = fromLowest? "ASC":"DESC";

        String sqlQuery = "SELECT * FROM " + Properties.tableName +  " WHERE category = ? ORDER BY price "+order;

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, category);

            List<Product> products = new ArrayList<>();
            ResultSet rs =  preparedStatement.executeQuery();

        while(rs.next()){
            products.add(extractProduct(rs));
        }

        return products;

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
    public static void deleteCategory(int id) {
        String sqlQuery = "DELETE FROM " + "Categories" + " WHERE id = ?";

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
    private static Product extractProduct(ResultSet rs) throws SQLException {
        Product product = new Product();

        product.setName(rs.getString("title"));
        product.setGroup(rs.getString("category"));
        product.setPrice(rs.getInt("price"));
        product.setAmount(rs.getInt("quantity"));
        product.setId(rs.getInt("id"));

        return product;

    }
}