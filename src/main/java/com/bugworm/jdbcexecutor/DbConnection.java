package com.bugworm.jdbcexecutor;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DbConnection{

    public static String url;
    public static String user;
    public static String pass;

    private static Connection connection = null;

    public static void close(){
        try{
            connection.close();
        }catch(Exception e){
            //何もしない
        }finally{
            connection = null;
        }
    }

    public static synchronized int executeUpdate(String query)throws SQLException{

        Statement statement = getConnection().createStatement();
        return statement.executeUpdate(query);
    }

    public static synchronized List<LinkedHashMap<String, Object>> executeQuery(String query)throws SQLException{

        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        Statement statement = getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        while(rs.next()){
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            for(int i = 1; i <= columnCount; i++){
                String name = meta.getColumnName(i);
                Object value = rs.getObject(i);
                map.put(name, value);
            }
            list.add(map);
        }
        return list;
    }

    public static synchronized void commit()throws SQLException{
        getConnection().commit();
    }

    public static synchronized void rollback()throws SQLException{
        getConnection().rollback();
    }

    private static Connection getConnection()throws SQLException{
        if(connection == null){
            connection = DriverManager.getConnection(url, user, pass);
            connection.setAutoCommit(false);
        }
        return connection;
    }
}
