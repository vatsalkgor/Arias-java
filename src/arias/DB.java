/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arias;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sqlite.SQLiteException;

/**
 *
 * @author vatsal
 */
public class DB {

    DB() {
        connect();
    }
    private static Connection con = null;

    private void connect() {
        try {
            String url = "jdbc:sqlite:arias.db";
            con = DriverManager.getConnection(url);
            System.out.println("Connecion successful");
        } catch (Exception e) {
            System.out.println("Something went wrong. Can not connect to database.");
        }
    }

    protected boolean insertDA(String month, int year, float rate) {
        String sql = "insert into da(month,year,rate) VALUES(?,?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, month);
            stmt.setInt(2, year);
            stmt.setFloat(3, rate);
            return stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected ResultSet getDA() {
        try {
            Statement stmt = con.createStatement();
            return stmt.executeQuery("select * from da");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected boolean removeDA(String m, int y, float r) {
        String sql = "delete from da where month=? and year=? and rate=?";
        try {
            PreparedStatement s = con.prepareStatement(sql);
            s.setString(1, m);
            s.setInt(2, y);
            s.setFloat(3, r);
            return s.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected int updateDA(String m, int y, float r) {
        String sql = "update da set rate=? where month=? and year=?";
        try {
            PreparedStatement s = con.prepareStatement(sql);
            s.setFloat(1, r);
            s.setString(2, m);
            s.setInt(3, y);
            return s.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return 0;
        }
    }

    protected int addPM(int level, long pay) {
        String sql = "insert into pm(level,pay) VALUES(?,?)";
        try {
            PreparedStatement s = con.prepareStatement(sql);
            s.setInt(1, level);
            s.setLong(2, pay);
            return s.executeUpdate();
        } catch (SQLiteException e) {
            System.out.println(e);
            e.printStackTrace();
            return -1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    protected boolean removePM(int level) {
        String sql = "delete from pm where level=?";
        try {
            PreparedStatement s = con.prepareStatement(sql);
            s.setInt(1, level);
            return s.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    protected ResultSet getPM() {
        try {
            Statement stmt = con.createStatement();
            return stmt.executeQuery("select * from pm");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected int updatePM(int level, long pay){
        String sql = "update pm set pay=? where level=?";
        try {
            PreparedStatement s = con.prepareStatement(sql);
            s.setLong(1, pay);
            s.setInt(2, level);
            return s.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    protected int setHRA(float f){
        String sql = "update hra set rate=? where id=1";
        try {
            PreparedStatement s = con.prepareStatement(sql);
            s.setFloat(1, f);
            return s.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    private void close() {
        try {
            con.close();
        } catch (Exception e) {
            System.out.println("Something Went wrong. Can not close connection");
        }
    }
}
