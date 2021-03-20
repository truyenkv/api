package company.core.helper;

import company.core.Util.Soldier;
import company.core.jdbcPostgree.PostGresDB;
import company.core.model.database.PartnerUserTable;

import java.sql.*;

public class PartnerUserHelper extends PostGresDB {
    Connection connection = ConnectDB();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String sql = null;

    public String getUserInforByEmail(String email) throws SQLException {
        String name = null;
        try {
            sql = "SELECT * FROM partner_users WHERE email = " + "'" + email + "'";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                name = resultSet.getString("name");
            }
        } catch (SQLException throwables) {
            System.out.println("Can not excute getUserInfor");
            System.out.println("current SQL is: " + sql);
            throwables.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return name;
    }

    public void insertUser(PartnerUserTable partnerUser) throws SQLException {
        sql = "INSERT INTO partner_users (email, name, created_at, updated_at)" + "VALUES (?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, partnerUser.getEmail());
            preparedStatement.setString(2, partnerUser.getName());
            preparedStatement.setTimestamp(3, Soldier.timestamp());
            preparedStatement.setTimestamp(4, Soldier.timestamp());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("Can not excute insertUser");
            System.out.println("current SQL is: " + sql);
            throwables.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
    }


}
