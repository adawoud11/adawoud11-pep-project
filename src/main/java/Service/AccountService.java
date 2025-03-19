package Service;

import DAO.AccountDAOImpl;
import Model.Account;
import ValidationRules.AccountValidationRules;
import ValidationRules.ValidationException;

public class AccountService {
    private final AccountDAOImpl accountDAOImpl = new AccountDAOImpl();

    public Account registerUser(Account account) throws ValidationException {

        validateRegistrationRules(account);
        return accountDAOImpl.registerUser(account);

    }

    public Account userlogin(Account account) throws ValidationException {

        validateLoginnRules(account);

        return accountDAOImpl.login(account);

    }

    private void validateRegistrationRules(Account account) throws ValidationException {
        AccountValidationRules.validateAccountObjectIsNotNully(account);
        AccountValidationRules.validatePasswordIsNotEmpty(account.getPassword());
        AccountValidationRules.validateUserNameIsNotEmpty(account.getUsername());
        AccountValidationRules.validateUserNameIsUnique(account.getUsername(), accountDAOImpl);
        AccountValidationRules.validatePasswordLength(account.getPassword());

    }

    private void validateLoginnRules(Account account) throws ValidationException {
        AccountValidationRules.validateUserNameAndPass(account, accountDAOImpl);
        AccountValidationRules.validateAccountObjectIsNotNully(account);
        AccountValidationRules.validatePasswordIsNotEmpty(account.getPassword());
        AccountValidationRules.validateUserNameIsExists(account.getUsername(), accountDAOImpl);
        AccountValidationRules.validateUserNameIsNotEmpty(account.getUsername());
        AccountValidationRules.validatePasswordLength(account.getPassword());
        AccountValidationRules.validateUserNameIsUnique(account.getUsername(), accountDAOImpl);
    }
}
