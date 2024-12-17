package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account){
        
        //check that the username is not blank
        if(account.getUsername() == null || account.getUsername() == ""){
            return null;
        }
        //check that the password length is at least 4 characters
        if(account.getPassword().length() < 4){
            return null;
        }
        //check that username does not already exist
        if(accountDAO.getAccountByUsername(account.getUsername()) == null){
            return accountDAO.insertAccount(account);
        }else{
            //account exists
            return null;
        }

    }

    public Account login(Account account){
        Account fullAccount = accountDAO.getAccountByUsername(account.getUsername());
        
        //check that username exists and the passwords match
        if(fullAccount != null && fullAccount.getPassword().equals(account.getPassword())){
            return fullAccount;
        }else{
            return null;
        }
    }

}
