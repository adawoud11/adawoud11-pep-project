package DAO;

import java.util.List;

import Model.Message;

interface IMessageDAO {
    // Create New Message
    Message createNewMessage(Message msg);

    // Get All Messages
    List<Message> getAllMessages();

    // Get One Message Given Message Id
    Message getMessageById(int message_id);

    // Delete a Message Given Message Id
    Message deleteMessageById(int message_id);

    // Update Message Given Message Id
    Integer updateMessageTextById(String msgText, Integer msgId);

    // Get All Messages From User Given Account Id
    List<Message> getAllMessagesByAccountId(int account_id);

}