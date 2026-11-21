
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL =
        "jdbc:mysql://localhost:3306/web_mp2";

    private static final String USER = "root";
    private static final String SENHA = "";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("MySQL Driver carregado com sucesso!");

        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "MySQL Connector/J não encontrado no classpath do Tomcat.",e
            );
        }

        return DriverManager.getConnection(URL, USER, SENHA);
    }
}
