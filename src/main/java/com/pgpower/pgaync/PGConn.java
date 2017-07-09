/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pgpower.pgaync;

import com.github.pgasync.ConnectionPoolBuilder;
import com.github.pgasync.Db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author PRanjan3
 */
public class PGConn {

    private  Connection conn;

  
  

    public  Connection  getConn() {
        try {

            if (conn == null) {
//            String url = "jdbc:postgresql://localhost/postgres";
//            Properties props = new Properties();
//            props.setProperty("user", "postgres");
//            props.setProperty("password", "postgres");
//            props.setProperty("ssl", "false");
//            Connection conn = DriverManager.getConnection(url, props);

                String url = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres&ssl=false";
                conn =  DriverManager.getConnection(url);
                System.out.println(conn + "");
             
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
       return conn;
    }
    
    
    public Db getAsyncConn(){
        Db db = new ConnectionPoolBuilder()
        .hostname("localhost")
        .port(5432)
        .database("postgres")
        .username("postgres")
        .password("postgres")
        .poolSize(20)
        .build();
        return db;
    }

    public static void main(String[] args) {
        
        new PGConn().getAsyncConn().queryRows("select unnest('{ hello, world }'::text[] as message)")
    .map(row -> row.getString("message"))
    .subscribe(System.out::println);

    }
}
