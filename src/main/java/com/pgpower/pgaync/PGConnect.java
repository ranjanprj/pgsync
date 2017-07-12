/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pgpower.pgaync;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author PRanjan3
 */
public class PGConnect {

    private static Connection c = null;

    public static Connection getConnection() {

        try {
            if (c == null) {
                Class.forName("org.postgresql.Driver");
                c = DriverManager
                        .getConnection("jdbc:postgresql://localhost:5432/postgres",
                                "<UID>", "<PWD>");
            }

            return c;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return null;
    }
}
