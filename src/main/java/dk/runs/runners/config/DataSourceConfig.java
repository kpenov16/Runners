package dk.runs.runners.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConfig {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    static {
        config.setJdbcUrl("jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s185144?");
        config.setUsername("s185144");
        config.setPassword("XFfpicTFLy2RzYknRgLMO");
        config.addDataSourceProperty("cachePrepStmts","true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(3);

        dataSource = new HikariDataSource(config);
    }

    private DataSourceConfig(){}

    public static Connection getConnection()throws SQLException {
        return dataSource.getConnection();
    }
}
