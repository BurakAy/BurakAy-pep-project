package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
  public Message createMessage(Message message) {
    Connection connection = ConnectionUtil.getConnection();
    try {
      String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
      PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setInt(1, message.getPosted_by());
      preparedStatement.setString(2, message.getMessage_text());
      preparedStatement.setLong(3, message.getTime_posted_epoch());
      preparedStatement.executeUpdate();

      ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
      if (pkeyResultSet.next()) {
        int generated_user_id = (int) pkeyResultSet.getInt(1);
        return new Message(generated_user_id, message.getPosted_by(), message.getMessage_text(),
            message.getTime_posted_epoch());
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public List<Message> getAllMessages() {
    Connection connection = ConnectionUtil.getConnection();
    List<Message> messages = new ArrayList<>();
    try {
      String sql = "SELECT * FROM message";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);

      ResultSet queryResults = preparedStatement.executeQuery();
      while (queryResults.next()) {
        Message message = new Message(queryResults.getInt("message_id"), queryResults.getInt("posted_by"),
            queryResults.getString("message_text"), queryResults.getLong("time_posted_epoch"));
        messages.add(message);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return messages;
  }

  public Message getMessageByID(int message_id) {
    Connection connection = ConnectionUtil.getConnection();
    try {
      String sql = "SELECT * FROM message WHERE message_id = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, message_id);

      ResultSet queryResults = preparedStatement.executeQuery();
      while (queryResults.next()) {
        Message message = new Message(queryResults.getInt("message_id"), queryResults.getInt("posted_by"),
            queryResults.getString("message_text"), queryResults.getLong("time_posted_epoch"));
        return message;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public int deleteMessage(int message_id) {
    Connection connection = ConnectionUtil.getConnection();
    try {
      String sql = "DELETE FROM message WHERE message_id = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, message_id);

      int messageExisted = preparedStatement.executeUpdate();
      return messageExisted;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return 0;
  }

  public void updatMessage(int message_id, String message_text) {
    Connection connection = ConnectionUtil.getConnection();
    try {
      String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, message_text);
      preparedStatement.setInt(2, message_id);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public List<Message> getAllAccountMessages(int account_id) {
    Connection connection = ConnectionUtil.getConnection();
    List<Message> messages = new ArrayList<>();
    try {
      String sql = "SELECT * FROM message WHERE posted_by = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, account_id);

      ResultSet queryResults = preparedStatement.executeQuery();
      while (queryResults.next()) {
        Message message = new Message(queryResults.getInt("message_id"), queryResults.getInt("posted_by"),
            queryResults.getString("message_text"), queryResults.getLong("time_posted_epoch"));
        messages.add(message);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return messages;
  }
}
