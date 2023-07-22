import java.sql.*;

public class DBUtils {

    private Connection connection;
    private final String URL = "jdbc:oracle:thin:@calvin.humber.ca:1521:grok";
    private final String USERNAME = "n01587845";
    private final String PASSWORD = "oracle";

    public DBUtils() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    public Connection getConnection() {
        return connection;
    }

    public PreparedStatement getPreparedViemStatement() throws SQLException {
        String pstmtViem = "SELECT * FROM Staff WHERE id = ?";
        return connection.prepareStatement(pstmtViem);
    }

    public PreparedStatement getPreparedInsertStatment() throws SQLException {
        String pstmtInsert = "INSERT INTO Staff(id, lastName, firstName, mi, address, city, state, telephone)" +
                                "Values(?, ?, ?, ?, ?, ?, ?, ?)";
        return connection.prepareStatement(pstmtInsert);
    }

    public PreparedStatement getPreparedUpdateStatement() throws SQLException {
        String pstmtUpdate = "UPDATE Staff SET lastName = ?, firstName = ?, mi = ?, " +
                                "address = ?, city = ?, state = ?, telephone = ?" +
                                "WHERE id = ?";
        return connection.prepareStatement(pstmtUpdate);
    }
    public PreparedStatement getPreparedDeleteStatement() throws SQLException {
        String pstmtDelete = "DELETE FROM Staff WHERE id = ?";
        return connection.prepareStatement(pstmtDelete);
    }

    public boolean idExsits(String id) throws SQLException {
        PreparedStatement checkId = connection.prepareStatement("SELECT * FROM Staff WHERE id = ?");
        checkId.setString(1, id);
        ResultSet rs = checkId.executeQuery();

        return rs.next();
    }

    public void closeConncetion() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error: Unable to close the database connection.");
                e.printStackTrace();
            }
        }
    }


}
