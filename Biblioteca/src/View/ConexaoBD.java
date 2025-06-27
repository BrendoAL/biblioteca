package View;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    public static Connection getConexao() {

        String user = "root";
        String password = "Gear5";
        String url = "jdbc:mysql://localhost:3306/biblioteca";

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return null;

    }

}
