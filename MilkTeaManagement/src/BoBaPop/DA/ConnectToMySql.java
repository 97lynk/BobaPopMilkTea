package BoBaPop.DA;

import BoBaPop.Util.MessageBox;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import tray.notification.NotificationType;

public class ConnectToMySql {

    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "db_milktea";

    private static final String DB_URL = String.format("jdbc:mysql://%s:%s/%s?useUnicode=yes&characterEncoding=UTF-8", DB_HOST, DB_PORT, DB_NAME);

    //  Database credentials
    private static final String USER = "root";
    private static final String PASS = "123456";

    public static DSLContext context = null;

    public static void initialize() {
        try {
            context = usingDSLContext();
        } catch (SQLException | ClassNotFoundException ex) {
            MessageBox.showAndWait(
                    "Excepttion: " + ex.getClass().getSimpleName(),
                    ex.getMessage(),
                    NotificationType.ERROR);
        }
    }

    public static DSLContext usingDSLContext() throws SQLException, ClassNotFoundException {
        return DSL.using(DB_URL, USER, PASS);
    }

    public static Connection createConnection(String user, String password) throws SQLException, ClassNotFoundException {
        Connection connection = null;

        //Register JDBC driver
        Class.forName(JDBC_DRIVER);

        //Open a connection
        System.out.println("Connecting to a selected database...");
        connection = DriverManager.getConnection(DB_URL, user, password);
        if (connection != null) {
            System.out.println("Connected database successfully...");
        }

        //Handle errors for Class.forName
        return connection;
    }

    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        return createConnection(USER, PASS);

    }

    public static DSLContext usingDSLContext(String user, String password) throws SQLException, ClassNotFoundException {
        return DSL.using(createConnection(user, password), SQLDialect.MYSQL);
    }

}
