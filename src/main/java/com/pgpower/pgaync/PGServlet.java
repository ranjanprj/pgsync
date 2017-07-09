/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pgpower.pgaync;

import com.github.pgasync.Db;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author PRanjan3
 */
public class PGServlet extends HttpServlet {

    private static Connection c = null;
    final private Random rand = new Random();
    private static Db db;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest1(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {

            int min = 1;
            int max = 9999;
            int random = rand.nextInt(max - min + 1) + min;
            String sql = "select " + random + " as random_num ,hpf from generate_series(1," + random + ") as t(hpf) where is_prime(hpf) and " + random + " % hpf = 0 order by hpf desc limit 1";

            if (c == null) {
                c = PGConnect.getConnection();

            }
//            s.execute("select version();");
//           ResultSet rs = s.getResultSet();
//           while(rs.next()){
//               
//               response.getWriter().write(rs.getString(1));
//           }  
            Statement s = c.createStatement();
            s.execute(sql);
            ResultSet r = s.getResultSet();

            while (r.next()) {

                String result = "{\"random_num\"" + ":" + r.getString("random_num") + " , \"hpf\"" + ":" + r.getInt("hpf") + "}";

                out.write(result);
            }
            r.close();
            s.close();
            r = null;
            s = null;
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {

            int min = 1;
            int max = 9999;
            int random = rand.nextInt(max - min + 1) + min;
            String sql = "select " + random + " as random_num ,hpf from generate_series(1," + random + ") as t(hpf) where is_prime(hpf) and " + random + " % hpf = 0 order by hpf desc limit 1";
            System.out.println("EXECUTING***************");
            if (db == null) {
                db = new PGConn().getAsyncConn();
            }
            final String res = "{\"random_num\"" + ": %d  , \"hpf\"" + ": %d}";
            db.queryRows(sql).subscribe(result -> out.printf(
            String.format(res, result.getInt("random_num"), result.getInt("hpf"))
            ));
            db.queryRows(sql).map(row -> row.getInt("random_num")).subscribe(System.out::println);

            System.out.println("DONE***************");

//            while(r.next()){
//                
//               String result = "{\"random_num\"" + ":" + r.getString("random_num")+" , \"hpf\"" + ":" +r.getInt("hpf")+"}";
//               
//               out.write(result);
//            }
//            r.close();
//            s.close();
//            r = null;
//            s = null;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(PGServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(PGServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
