package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
  public Account createAccount(Account account) {
    Connection connection = ConnectionUtil.getConnection();
    try {
      String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
      PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, account.getUsername());
      preparedStatement.setString(2, account.getPassword());
      preparedStatement.executeUpdate();

      ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
      if (pkeyResultSet.next()) {
        int generated_user_id = (int) pkeyResultSet.getInt(1);
        return new Account(generated_user_id, account.getUsername(), account.getPassword());
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public Account verifyLogin(Account account) {
    Connection connection = ConnectionUtil.getConnection();
    try {
      String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, account.getUsername());
      preparedStatement.setString(2, account.getPassword());

      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Account verifiedAccount = new Account(resultSet.getInt("account_id"), resultSet.getString("username"),
            resultSet.getString("password"));
        return verifiedAccount;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }
}
