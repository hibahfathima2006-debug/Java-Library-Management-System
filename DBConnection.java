package src;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/library_db";
        String user = "root";
        String password = "Library@2026Safe";

        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        try {
            Connection con = getConnection();
            System.out.println("MySQL Connected Successfully!");
            con.close();
        } catch (Exception e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}