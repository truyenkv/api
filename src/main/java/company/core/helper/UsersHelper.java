package company.core.helper;

import company.core.jdbcPostgree.PostGresDB;

import java.sql.*;

public class UsersHelper extends PostGresDB {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String sql = null;

    public String GetConfirmationTokenByEmail(Connection connection, String schema, String email) throws SQLException {
        String token = null;
        schema = PostGresDB.isSchema(schema);
        try {
            sql = "SELECT confirmation_token FROM " + '"'+schema+'"' + ".users " +  "WHERE email = " + "'" + email + "'";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                token = resultSet.getString("confirmation_token");
            }
        } catch (SQLException throwables) {
            System.out.println("Can not excute getTokenByEmail");
            System.out.println("current SQL is: " + sql);
            throwables.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return token;
    }

    public String GetKYCCodeByEmail(Connection connection, String schema, String email) throws SQLException {
        String kycLevel = null;
        schema = PostGresDB.isSchema(schema);
        try {
            sql = "SELECT kyc_level FROM " + '"'+schema+'"' + ".users " +  "WHERE email = " + "'" + email + "'";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                kycLevel = resultSet.getString("kyc_level");
            }
        } catch (SQLException throwables) {
            System.out.println("Can not excute getKYCCodebyEmal");
            System.out.println("current SQL is: " + sql);
            throwables.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return kycLevel;
    }

    public void TurnOf2FAByEmail(Connection connection, String schema, Boolean isLogin, String email) throws SQLException {
        schema = PostGresDB.isSchema(schema);
        try {
            sql = "UPDATE " + '"'+schema+'"' + ".users " + "SET otp_required_for_login = ? " +  "WHERE email = " + "'" + email + "'";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, isLogin);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("Can not excute TurnOf2FAByEmail");
            System.out.println("current SQL is: " + sql);
            throwables.printStackTrace();
        } finally {
            preparedStatement.close();
        }
    }

    public boolean OTPRequireLogin(Connection connection, String schema, String email) {
        schema = PostGresDB.isSchema(schema);
        sql = "SELECT otp_required_for_login FROM " + '"' + schema + '"' + ".users " + "WHERE email = " + "'" + email + "'";
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                return resultSet.getBoolean("otp_required_for_login");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return true;
    }

}
