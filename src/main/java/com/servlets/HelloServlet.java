package com.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.annotation.WebServlet;

@WebServlet(name="HelloServlet", urlPatterns={"/"})
public class HelloServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Output Countries Servlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println(getHello(request, response));
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public String getHello(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String query = "SELECT value FROM messages WHERE id = 1";
        DbConnector sdw = new DbConnector();
        try (Connection conn = sdw.getPoolConnection()) {
            Statement stat = conn.createStatement();
            ResultSet resultSet = stat.executeQuery(query);
            resultSet.next();
            return resultSet.getString("value");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}