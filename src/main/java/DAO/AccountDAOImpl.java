package DAO;

import java.sql.Statement;

import static org.mockito.ArgumentMatchers.nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAOImpl implements IAccountDAO {

    @Override
    public Account registerUser(Account account) {

        //
        String sql = "INSERT INTO account (username,password) VALUES (?,?)";
        Account newUserAccount = null;
        try (Connection con = ConnectionUtil.getConnection();
                PreparedStatement pStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pStatement.setString(1, account.getUsername());
            pStatement.setString(2, account.getPassword());
            int rowCount = pStatement.executeUpdate();
            if (rowCount > 0) {
                try (ResultSet res = pStatement.getGeneratedKeys();) {
                    if (res.next()) {
                        newUserAccount = new Account();
                        newUserAccount.setUsername(account.getUsername());
                        newUserAccount.setPassword(account.getPassword());
                        newUserAccount.setAccount_id(res.getInt(1));
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return newUserAccount;
    }

    @Override
    public Boolean isUniqueUserName(String userName) {
        String sql = "SELECT username FROM account WHERE username = ?;";
        try (Connection con = ConnectionUtil.getConnection();
                PreparedStatement pStatement = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)) {
            pStatement.setString(1, userName);
            try (ResultSet res = pStatement.executeQuery();) {
                if (res.last()) {
                    return res.getRow() == 1 ? true : false;
                } else
                    return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

    }

    @Override
    public Account login(Account account) {
        Account userAccount = null;
        String sql = "SELECT account_id, username, password  FROM account WHERE username = ? AND password = ?;";
        try (Connection con = ConnectionUtil.getConnection();
                PreparedStatement pStatement = con.prepareStatement(sql)) {
            pStatement.setString(1, account.getUsername());
            pStatement.setString(2, account.getPassword());

            try (ResultSet res = pStatement.executeQuery();) {
                if (res.next()) {
                    userAccount = new Account();
                    userAccount.setAccount_id(res.getInt("account_id"));
                    userAccount.setPassword(res.getString("password"));
                    userAccount.setUsername(res.getString("username"));
                }
            }
            return userAccount;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Account getAccountById(int Id) {
        String sql = "SELECT account_id, username FROM account WHERE account_id = ?;";
        Account account = null;
        try (Connection con = ConnectionUtil.getConnection();
                PreparedStatement pStatement = con.prepareStatement(sql)) {
            pStatement.setInt(1, Id);

            try (ResultSet res = pStatement.executeQuery();) {
                if (res.next()) {
                    account = new Account();
                    account.setAccount_id(res.getInt("account_id"));
                    account.setUsername(res.getString("username"));
                }
            }
            return account;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean checkUsernameIsExists(String username) {
        String sql = "SELECT username FROM account WHERE username = ?;";
        try (Connection con = ConnectionUtil.getConnection();
                PreparedStatement pStatement = con.prepareStatement(sql)) {
            pStatement.setString(1, username);
            try (ResultSet res = pStatement.executeQuery();) {
                if (res.next()) {
                    return true;
                } else
                    return false;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
