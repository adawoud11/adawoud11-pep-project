package ValidationRules;

import DAO.AccountDAOImpl;
import DAO.MessageDAOImpl;
import Model.Account;
import Model.Message;

public abstract class MessageValidationRules {
    private static final AccountDAOImpl ACCOUNT_DAO_IMPL = new AccountDAOImpl();
    private static final MessageDAOImpl MESSAGE_DAO_IMPL = new MessageDAOImpl();

    // Constants using final static for error messages and configuration
    private static final String MESSAGE_IS_NULL_ERROR = "Message Object can not be Null";
    private static final String MESSAGE_TEXT_BLANK_ERROR = "Message text cannot be blank";
    private static final String MESSAGE_TEXT_LENGTH_ERROR = "Message text must be 255 characters or less";
    private static final String USER_NOT_FOUND_ERROR = "Posted_by must refer to an existing user";
    private static final String MESSAGE_NOT_FOUND_ERROR = "Message ID does not exist";
    private static final int MAX_MESSAGE_LENGTH = 255;

    /**
     * Validates the conditions for creating a new message.
     * Throws ValidationException if any of following condition is not met:
     * If message_text is blank
     * exceeds 255 characters
     * posted_by does not exist
     * message_id does not exist
     */
    // Check if message object is null
    public static final void validateMessageObjectIsNull(Message msg) throws ValidationException {

        if (null == msg) {
            throw new ValidationException(MESSAGE_IS_NULL_ERROR);
        }
    }

    // Check if message_text is null or blank
    public static final void validateMessageTextInNotNull(String message_text) throws ValidationException {

        if (message_text == null || message_text.isBlank()) {
            throw new ValidationException(MESSAGE_TEXT_BLANK_ERROR);
        }
    }

    // Check if message_text exceeds 255 characters
    public static final void validateMessageTextNotMoreThan255(String message_text) throws ValidationException {
        if (message_text.length() > MAX_MESSAGE_LENGTH) {
            throw new ValidationException(MESSAGE_TEXT_LENGTH_ERROR);
        }
    }

    // Check if posted_by refers to an existing user
    public static final void validateUserIsExisting(int posted_by) throws ValidationException {
        Account account = ACCOUNT_DAO_IMPL.getAccountById(posted_by);
        if (null == account || account.getAccount_id() < 1) {
            throw new ValidationException(USER_NOT_FOUND_ERROR);
        }

    }

    // Check if message_id exists
    public static final void validateMessageIdIsExists(int message_id) throws ValidationException {
        Message msg = MESSAGE_DAO_IMPL.getMessageById(message_id);
        if (null == msg || msg.getMessage_id() < 1) {
            throw new ValidationException(MESSAGE_NOT_FOUND_ERROR);
        }
    }

}