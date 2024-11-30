package Service;

import Model.Account;
import Model.Message;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    AccountDAO accountDAO;

    /**
     * No-args constructor for an accountService instantiates a plain accountDAO.
     */
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor for an accountService when a accountDAO is provided.
     * This is used for when a mock accountDAO that exhibits mock behavior is used
     * in the test cases.
     * This would allow the testing of AccountService independently of AccountDAO.
     * 
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Use the AccountDAO to add a new account to the database.
     *
     * @param account an object representing a new Account.
     * @return the newly added account if the add operation was successful,
     *         including
     *         the account_id.
     */
    public Account addAccount(Account account) {
        // check to see if username not in use
        Account dbAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (dbAccount != null) {
            return null;
        }
        // else add new account
        return accountDAO.insertAccount(account);
    }

    /**
     * Use the AccountDAO to get an existing account using the poster id from the
     * database.
     *
     * @param id an object representing a poster id.
     * @return the newly added account if the get operation was successful,
     *         including
     *         the account_id.
     */
    public Account getAccountId(int id) {
        Account dbAccount = accountDAO.getAccountById(id);
        // if account retrieval(id) fails
        if (dbAccount == null) {
            return null;
        }
        return dbAccount;
    }

    /**
     * Use the AccountDAO to get an existing account using the username from the
     * database.
     *
     * @param account an object representing a new Account.
     * @return the newly added account if the get operation was successful,
     *         including
     *         the account_id.
     */
    public Account getAccount(Account account) {
        Account dbAccount = accountDAO.getAccountByUsername(account.getUsername());
        // if account retrieval(username) fails or password does not match
        if (dbAccount == null) {
            return null;
        } else if (!dbAccount.getPassword().equals(account.getPassword())) {
            return null;
        }
        return dbAccount;
    }

    /**
     * Use the AccountDAO to retrive all account messages.
     *
     * @return messages with id in the database.
     */
    public List<Message> getAccountMessages(int account_id) {
        return accountDAO.getAccountMessages(account_id);
    }

}
