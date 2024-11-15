package br.com.alura.bytebank;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection recuperarConexao(){

        try {

            /*
            Conexão simples com JDBC
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/byte_bank?user=root&password=Tommy@001");*/

            /*Conexão com pool de conexão*/
            return createDataSource().getConnection();

        } catch (SQLException e){

            throw  new RuntimeException(e);

        }
    }

    private HikariDataSource createDataSource(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/byte_bank");
        config.setUsername("root");
        config.setPassword("Tommy@001");
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }
}
