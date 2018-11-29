package com.roczhou.util;

import java.sql.Connection;
import com.roczhou.Constants;

import com.alibaba.druid.pool.DruidDataSource;
/**
 * Created by ZhouPeng on 2018/2/24.
 */
public class ConnectionFactory {

    private static DruidDataSource dataSource = null;

    static {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://"+Constants.DB_HOST+":"+Constants.DB_PORT+"/idcard?useUnicode=true&characterEncoding=utf8&useSSL=false&allowMultiQueries=true";
            String user = Constants.DB_USERNAME;
            String password = Constants.DB_PWD;

            dataSource = new DruidDataSource();
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
            dataSource.setInitialSize(5);
            dataSource.setMinIdle(10);
            dataSource.setMaxActive(100);

            dataSource.setPoolPreparedStatements(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized Connection getConnection() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

}
