package BoBaPop.DA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class ConnectToMySql {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/coffeemilkteamanager?useUnicode=yes&characterEncoding=UTF-8";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "lumia435";

    public static DSLContext usingDSLContext() throws Exception {
        try {
            return DSL.using(createConnection(), SQLDialect.MYSQL);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Connection createConnection(String user, String password) throws SQLException, Exception {
        Connection connection = null;
        try {
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            System.out.println("Connecting to a selected database...");
            connection = DriverManager.getConnection(DB_URL, user, password);
            if (connection != null) {
                System.out.println("Connected database successfully...");
            }
        } catch (SQLException se) {
            //Handle errors for JDBC
            throw se;
        } catch (Exception e) {
            //Handle errors for Class.forName
            throw e;
        }
        return connection;
    }

    public static Connection createConnection() throws SQLException, Exception {
        try {
            return createConnection(USER, PASS);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static DSLContext usingDSLContext(String user, String password) throws Exception {
        try {
            return DSL.using(createConnection(user, password), SQLDialect.MYSQL);
        } catch (Exception ex) {
            throw ex;
        }
    }

}
