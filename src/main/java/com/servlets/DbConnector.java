package com.servlets;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DbConnector {
    private String USER;
    private String PSW;
    private String  urlJDBC;
    private Connection connection;
    private Properties prop;

    public DbConnector() throws IOException {
        prop = new Properties();
        try {
            try (InputStream in =
                         Files.newInputStream(Paths.get(
                                 Thread.currentThread().getContextClassLoader()
                                         .getResource("database.properties").toURI())))
            {
                prop.load(in);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlJDBC = prop.getProperty("jdbc.urlJDBC");
        USER = prop.getProperty("jdbc.username");
        PSW = prop.getProperty("jdbc.password");
    }

    public Connection getPoolConnection() throws ExecutionException, InterruptedException {
        PoolProperties p = new PoolProperties();
        p.setUrl(urlJDBC);
        p.setDriverClassName("org.postgresql.Driver");
        p.setUsername(USER);
        p.setPassword(PSW);
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        DataSource datasource = new DataSource();
        datasource.setPoolProperties(p);
        Future<Connection> future = null;
        try {
            future = datasource.getConnectionAsync();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return future.get();
    }
}

