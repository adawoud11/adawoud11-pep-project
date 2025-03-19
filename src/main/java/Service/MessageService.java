package Service;

import DAO.MessageDAOImpl;
import Model.Message;
import ValidationRules.MessageValidationRules;
import ValidationRules.ValidationException;
import java.util.List;

public class MessageService {
    // Immutable DAO reference, injected via constructor
    private final MessageDAOImpl messageDAO = new MessageDAOImpl();

    /**
     * Constructor with dependency injection for the DAO
     * 
     * @param messageDAO The DAO implementation to use
     */
    public MessageService() {
        if (messageDAO == null) {
            throw new IllegalArgumentException("MessageDAO cannot be null");
        }

    }

    /**
     * Creates a new message after validation
     * 
     * @param msg The message to create
     * @return The created message with its generated ID
     * @throws ValidationException If validation fails
     */
    public Message createMessage(Message msg) throws ValidationException {
        validateCreateMessage(msg);
        return messageDAO.createNewMessage(msg);
    }

    /**
     * Retrieves all messages
     * 
     * @return List of all messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Retrieves a message by its ID
     * 
     * @param messageId The ID of the message
     * @return The message, or null if not found
     */
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    /**
     * Deletes a message by its ID
     * 
     * @param messageId The ID of the message to delete
     * @return The deleted message, or null if not found
     */
    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    /**
     * Updates a message after validation
     * 
     * @param message_text The message with updated details
     * @param msgId
     * @return The updated message, or null if not found
     * @throws ValidationException If validation fails
     */
    public Message updateMessage(String message_text, Integer msgId) throws ValidationException {
        MessageValidationRules.validateMessageTextInNotNull(message_text);
        if (messageDAO.updateMessageTextById(message_text, msgId) > 0) {
            return messageDAO.getMessageById(msgId);
        } else
            return null;
    }

    /**
     * Retrieves all messages posted by a specific account
     * 
     * @param accountId The ID of the account
     * @return List of messages posted by the account
     */
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getAllMessagesByAccountId(accountId);
    }

    // --- Validation Methods ---

    /**
     * Validates a message for creation
     * 
     * @param msg The message to validate
     * @throws ValidationException If validation fails
     */
    private void validateCreateMessage(Message msg) throws ValidationException {
        MessageValidationRules.validateMessageObjectIsNull(msg);
        MessageValidationRules.validateMessageTextInNotNull(msg.getMessage_text());
        MessageValidationRules.validateMessageTextNotMoreThan255(msg.getMessage_text());
        MessageValidationRules.validateUserIsExisting(msg.getPosted_by());

    }

    /**
     * Validates a message for update
     * 
     * @param msg The message to validate
     * @throws ValidationException If validation fails
     */
    private void validateUpdateMessage(Message msg) throws ValidationException {
        MessageValidationRules.validateMessageObjectIsNull(msg);
        MessageValidationRules.validateMessageTextInNotNull(msg.getMessage_text());
        MessageValidationRules.validateMessageTextNotMoreThan255(msg.getMessage_text());
        MessageValidationRules.validateUserIsExisting(msg.getPosted_by());
        MessageValidationRules.validateMessageIdIsExists(msg.getMessage_id());
    }

}