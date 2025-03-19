package DAO;

import static org.mockito.ArgumentMatchers.nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAOImpl implements IMessageDAO {

    private final String INSERT_INTO_MESSAGE_QUERY = "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?);";
    private final String SELECT_ALL_MESSAGES_QUERY = "SELECT * FROM message";
    private final String SELECT_MESSAGE_BY_ID_QUERY = "SELECT * FROM message WHERE message_id = ?;";
    private final String DELETE_MESSAGE_BY_ID_QUERY = "DELETE FROM message WHERE message_id = ?;";
    private final String UPDATE_MESSAGE_TEXT_QUERY = "UPDATE message SET  message_text = ? WHERE message_id = ?;";

    /**
     * Creates a new message in the database and sets the auto-generated message_id.
     * 
     * @param msg The Message object to insert
     * @return The Message object with the generated message_id
     */
    @Override
    public Message createNewMessage(Message msg) {
        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        INSERT_INTO_MESSAGE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, msg.getPosted_by());
            pstmt.setString(2, msg.getMessage_text());
            pstmt.setLong(3, msg.getTime_posted_epoch());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        // msg = new Message();
                        msg.setMessage_id(rs.getInt(1));
                        // newMsg.setPosted_by(msg.getPosted_by());
                        // newMsg.setMessage_text(msg.getMessage_text());
                        // newMsg.setTime_posted_epoch(msg.getTime_posted_epoch());
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return msg;
    }

    /**
     * Retrieves all messages from the database.
     * 
     * @return A List of all Message objects
     */
    @Override
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        SELECT_ALL_MESSAGES_QUERY);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Message msg = new Message();
                msg.setMessage_id(rs.getInt("message_id"));
                msg.setPosted_by(rs.getInt("posted_by"));
                msg.setMessage_text(rs.getString("message_text"));
                msg.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                messages.add(msg);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return messages;
    }

    /**
     * Retrieves a message by its message_id.
     * 
     * @param message_id The ID of the message to retrieve
     * @return The Message object, or null if not found
     */
    @Override
    public Message getMessageById(int message_id) {
        Message msg = new Message();

        try (Connection conn = ConnectionUtil
                .getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_MESSAGE_BY_ID_QUERY)) {
            pstmt.setInt(1, message_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    msg.setMessage_id(rs.getInt("message_id"));
                    msg.setPosted_by(rs.getInt("posted_by"));
                    msg.setMessage_text(rs.getString("message_text"));
                    msg.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return msg;
        }
        return msg;
    }

    /**
     * Deletes a message by its message_id and returns the deleted message.
     * 
     * @param message_id The ID of the message to delete
     * @return The deleted Message object, or null if not found or deletion failed
     */
    @Override
    public Message deleteMessageById(int message_id) {
        Message msg = getMessageById(message_id);
        if (msg != null && msg.getMessage_id() > 0) {

            try (Connection conn = ConnectionUtil
                    .getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(DELETE_MESSAGE_BY_ID_QUERY)) {
                pstmt.setInt(1, message_id);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    msg = null; // Deletion failed
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return msg;
    }

    /**
     * Updates a message in the database using the provided Message object.
     * 
     * @param message_text The Message object with updated values and the message_id
     *                     to
     *                     update
     * @param msgId
     * @return The updated Message object, or null if no message was updated
     */
    @Override
    public Integer updateMessageTextById(String message_text, Integer msgId) {
        try {

            try (Connection conn = ConnectionUtil.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(UPDATE_MESSAGE_TEXT_QUERY)) {
                pstmt.setString(1, message_text);
                pstmt.setInt(2, msgId);
                int affectedRows = pstmt.executeUpdate();
                return affectedRows;

            } catch (SQLException e) {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves all messages posted by a specific account.
     * 
     * @param account_id The ID of the account whose messages to retrieve
     * @return A List of Message objects posted by the account
     */
    @Override
    public List<Message> getAllMessagesByAccountId(int account_id) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        try (Connection conn = ConnectionUtil
                .getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, account_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Message msg = new Message();
                    msg.setMessage_id(rs.getInt("message_id"));
                    msg.setPosted_by(rs.getInt("posted_by"));
                    msg.setMessage_text(rs.getString("message_text"));
                    msg.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                    messages.add(msg);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return messages;
    }
}