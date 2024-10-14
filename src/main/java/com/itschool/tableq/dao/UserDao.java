package com.itschool.tableq.dao;

import com.itschool.tableq.domain.User;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    HikariDataSource ds = new HikariDataSource();

    final static private String dbDriver = "org.postgresql.Driver";
    final static private String dbUrl = "jdbc:postgresql://192.168.1.168:5432/postgres";
    final static private String dbUser = "postgres";
    final static private String dbPassword = "1234";

    {
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword(dbPassword);
        ds.setDriverClassName(dbDriver);
    }

    private Connection connect() throws SQLException {
        try {
            Connection connection =ds.getConnection();
            //DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            System.out.println("DB Connection [성공]");
            return connection;
        }catch (SQLException e) {
            throw new SQLException("Failed to connect to database.",e);
        }
    }
    public List<User> selectSample(String sql){
        try(Connection dbConn = connect();
            Statement st = dbConn.createStatement();
            ResultSet rs = st.executeQuery(sql);)
        {// try-with-resoures 문


            List<User> resultList = new ArrayList<>();

            while (rs.next()) {
                String id = rs.getString("id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String nickName = rs.getString("nickname");
                String phone_number = rs.getString("phone_number");
                String created_at = rs.getString("created_at");
                String last_login_at = rs.getString("last_login_at");
                String address = rs.getString("address");
                String name = rs.getString("name");
                String social_type = rs.getString("social_type");
                String social_id = rs.getString("social_id");

                resultList.add(new User(Long.parseLong(id),email,password,nickName,phone_number,Timestamp.valueOf(created_at)
                        ,Timestamp.valueOf(last_login_at),address,name,social_type,social_id));
            }

            return resultList;


        }catch (SQLException e) {
            throw new RuntimeException("Failed to select data from database.",e);
        }
    }

}
