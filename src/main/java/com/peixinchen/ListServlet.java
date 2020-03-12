package com.peixinchen;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/list")
public class ListServlet extends HttpServlet {
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setPort(3306);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("");
        mysqlDataSource.setDatabaseName("tangshi");
        mysqlDataSource.setUseSSL(false);
        mysqlDataSource.setCharacterEncoding("utf8");

        dataSource = mysqlDataSource;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<h1>唐诗三百首</h1>");
        out.println("<ol>");
        List<String> titles = getTitlesFromDB();
        for (String title : titles) {
            out.println("<li>" + title + "</li>");
        }
        out.println("</ol>");
    }

    private List<String> getTitlesFromDB() {
        List<String> titles = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT title FROM tangshi ORDER BY id";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String title = rs.getString("title");
                        titles.add(title);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return titles;
    }
}
