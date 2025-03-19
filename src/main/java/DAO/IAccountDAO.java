
package DAO;

import Model.Account;

interface IAccountDAO {

    Account registerUser(Account account);

    Boolean isUniqueUserName(String userName);

    Account login(Account account);

    Account getAccountById(int Id);

    Boolean checkUsernameIsExists(String username);
}