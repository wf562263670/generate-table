package com.generate.utils;


import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Map;

public class DBConnection {
    private final static Logger logger = Logger.getLogger(DBConnection.class);
    private final static Map<Object, Object> params = FileUtil.getConfig();
    private final static String driver = params.get("jdbc.driver").toString();
    private final static String url = params.get("jdbc.url").toString();
    private final static String username = params.get("jdbc.username").toString();
    private final static String password = params.get("jdbc.password").toString();

    public ResultSet executeQuery(String sql) {
        Connection conn = null;
        Statement state = null;
        ResultSet rs = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            state = conn.createStatement();
            rs = state.executeQuery(sql);
            logger.info(sql);
            return rs;
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(sql);
        }
        return null;
    }

    public boolean execute(String sql) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement state = conn.createStatement()) {
            Class.forName(driver);
            logger.warn(sql);
            state.execute(sql);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(sql);
            return false;
        }
    }

    public int executeUpdate(String sql) {
        Connection conn = null;
        Statement state = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            state = conn.createStatement();
            logger.warn(sql);
            return state.executeUpdate(sql);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(sql);
        } finally {
            close(conn, state);
        }
        return 0;
    }

    public void close(Connection conn, Statement state) {
        try {
            if (state != null) {
                state.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
