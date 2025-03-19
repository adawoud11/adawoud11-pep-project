package ValidationRules;

import DAO.AccountDAOImpl;
import Model.Account;

public abstract class AccountValidationRules {

    public static final void validateAccountObjectIsNotNully(Account account) throws ValidationException {
        if (null == account) {
            throw new ValidationException("Account object is null...");
        }
    }

    public static final void validateUserNameIsNotEmpty(String userName) throws ValidationException {
        if (userName.isBlank()) {
            throw new ValidationException("User name is empty...");
        }
    }

    public static final void validateUserNameIsUnique(String userName, AccountDAOImpl accountDAOImpl)
            throws ValidationException {
        if (!accountDAOImpl.isUniqueUserName(userName)) {
            throw new ValidationException("User name is not Unique...");
        }
    }

    public static final void validateUserNameIsExists(String userName, AccountDAOImpl accountDAOImpl)
            throws ValidationException {
        if (!accountDAOImpl.isUniqueUserName(userName)) {
            throw new ValidationException("User name deos not exists...");
        }
    }

    public static final void validateUserNameAndPass(Account account,
            AccountDAOImpl accountDAOImpl)
            throws ValidationException {
        if (null == accountDAOImpl.login(account)) {
            throw new ValidationException("Wrong user name or password...");
        }
    }

    public static final void validatePasswordLength(String password) throws ValidationException {
        if (password.length() < 4) {
            throw new ValidationException("Passowrd is less than 4 chars...");
        }
    }

    public static final void validatePasswordIsNotEmpty(String password) throws ValidationException {
        if (password.isBlank()) {
            throw new ValidationException("Passowrd is empty...");
        }
    }
}
