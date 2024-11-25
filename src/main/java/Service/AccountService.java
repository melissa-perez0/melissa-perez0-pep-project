package Service;

import Model.Account;
import DAO.AccountDAO;


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
     * @return the newly added account if the add operation was successful, including
     *         the account_id.
     */
    public Account addAccount(Account account) {
        // check to see if username not in use
        Account usernameInUse = accountDAO.getAccountByUsername(account.getUsername());
        if(usernameInUse != null) {
            return null;
        }
        // else add new account
        return accountDAO.insertAccount(account);
    }

}
