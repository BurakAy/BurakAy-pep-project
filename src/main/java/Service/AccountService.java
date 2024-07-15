package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
  AccountDAO accountDAO;

  public AccountService() {
    this.accountDAO = new AccountDAO();
  }

  public AccountService(AccountDAO accountDAO) {
    this.accountDAO = accountDAO;
  }

  public Account createAccount(Account account) {
    return accountDAO.createAccount(account);
  }

  public Account verifyLogin(Account account) {
    return accountDAO.verifyLogin(account);
  }
}
