package company.core.jdbcPostgree;
import company.environment.Environment;
import org.aeonbits.owner.ConfigFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class PostGresDB {

    /*
     * Owner: TruyenKV
     * Create the connection with Database
     * Input get envir name form command line
     * */


    static String env = System.getProperty("envir");
    static String currentSchema = System.getProperty("schema");
    private static Connection connect = null;
    static Environment envir = null;

    public PostGresDB() {
        ConfigFactory.setProperty("env", env);
        envir = ConfigFactory.create(Environment.class);
    }

    /* Create connection by without any param*/
    public static Connection ConnectDB() {
        try {
            String jdbc = envir.dbUrl() + envir.dbPort() + envir.ssoDBName() + "?currentSchema=" + currentSchema;
            connect = DriverManager.getConnection(jdbc, envir.dbUser(), envir.dbPass());
            System.out.println("Connect database successfull!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Connect fail!!");
        }
        return connect;
    }

    /* Check and close the connection*/
    public void CloseConnection() {
        if (connect != null) {
            try {
                connect.close();
                System.out.println("The connection had been closed");
            } catch (SQLException throwables) {
                System.out.println("Error is: " + throwables);
                System.out.println("Can't close the connection");
            }
        }
    }

    /*
    Create the connection with DataBase Name
    @Param: dbName */
    public static Connection ConnectDB(String dbName) {
        try {
            dbName = isDBName(dbName);
            String jdbc = envir.dbUrl() + envir.dbPort() + dbName + "?currentSchema=" + currentSchema;
            connect = DriverManager.getConnection(jdbc, envir.dbUser(), envir.dbPass());
            System.out.println("Connect database with DataBase name successful successfull!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Connect fail!!");
        }
        return connect;
    }

    /*
    Create the connection with DateName and SchemaName(tenant)
    @Param: dbName, schemaName
    */
    public static Connection ConnectDB(String dbName, String schemaName) {
//        InitConnection initConnection = new InitConnection(dbName, schemaName);
        try {
            dbName = isDBName(dbName);
            schemaName = isSchema(schemaName);
            String jdbc = envir.dbUrl() + envir.dbPort() + dbName + "?currentSchema=" + schemaName;
            connect = DriverManager.getConnection(jdbc, envir.dbUser(), envir.dbPass());
            System.out.println("Connect database with DataBase name successful and Schema Name successfull!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Connect fail!!");
        }
        return connect;
    }

    //will be handle by enum later
    public static String isDBName(String dbName) {
        DataBaseName dataBaseName = DataBaseName.valueOf(dbName.toUpperCase());
        if (dataBaseName==DataBaseName.SSODBNAME) {
            return envir.ssoDBName();
        }
        return null;
    }

    //will be handle by enum later
    public static String isSchema(String schema) {
        SchemaName schemaName = SchemaName.valueOf(schema.toUpperCase());
        if (schemaName==SchemaName.CEWL_ST) {
            return envir.cewlst();
        }
        return null;
    }


}
