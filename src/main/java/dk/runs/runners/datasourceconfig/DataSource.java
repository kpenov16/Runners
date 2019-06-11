package dk.runs.runners.datasourceconfig;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    static {
        config.setJdbcUrl("jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s133967?");
        config.setUsername("s133967");
        config.setPassword("8JPOJuQcgUpUVIVHY4S2H");
        config.addDataSourceProperty("cachePrepStmts","true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(20);

        dataSource = new HikariDataSource(config);
    }

    private DataSource(){}

    public static Connection getConnection()throws SQLException {
        return dataSource.getConnection();
    }
}
