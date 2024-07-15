package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
  MessageDAO messageDAO;

  public MessageService() {
    this.messageDAO = new MessageDAO();
  }

  public MessageService(MessageDAO messageDAO) {
    this.messageDAO = messageDAO;
  }

  public Message createMessage(Message message) {
    return messageDAO.createMessage(message);
  }

  public List<Message> getAllMessages() {
    return messageDAO.getAllMessages();
  }

  public Message getMessageByID(int message_id) {
    return messageDAO.getMessageByID(message_id);
  }

  public int deleteMessage(int message_id) {
    return messageDAO.deleteMessage(message_id);
  }

  public Message updateMessage(int message_id, String message_text) {
    if (messageDAO.getMessageByID(message_id) != null && message_text != null && !message_text.isEmpty() && message_text.length() <= 255) {
      messageDAO.updatMessage(message_id, message_text);
      return messageDAO.getMessageByID(message_id);
    }
    return null;
  }

  public List<Message> getAllAccountMessages(int account_id) {
    return messageDAO.getAllAccountMessages(account_id);
  }
}
