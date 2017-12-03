package manager;

import bank.Account;
import bank.CreditAccount;
import bank.SavingAccount;

import java.util.ArrayList;

/**
 * @author:czfshine
 * @date:2017/11/21 22:56
 */

public class AccountManager {

    private ArrayList<Account> as =new ArrayList<>(100);

    public boolean addAccount(Account ac){
        for(Account a: getAs()){
            if(a.getId()==ac.getId()){
                return false;
            }
        }
        getAs().add(ac);
        return true;
    }


    public Account getAccount(String id){
        for(Account a: getAs()){
            if(a.getId()==id){
                return a;
            }
        }
        return null;
    }


    public  boolean removeAccount(Account ac){
        for(Account a: getAs()){
            if(a.getId()==ac.getId()){
                if(a.getMoney()>0){
                    return false;
                }
                getAs().remove(a);
                return true;
            }
        }
        return false;
    }

    public double getTotalBalance(){
        double res=0;
        for(Account a: getAs()){
            res+=a.getMoney();
        }
        return res;
    }

    public double getTotalOverdraft(){
        double res=0;
        for(Account a: getAs()){
            if(a.getMoney()<0){
                res+=a.getMoney();
            }
        }
        return -res;
    }

    public  int getNumberOfAccount(){
        return getAs().size();
    }

    public int getNumberOfCreditAccount(){
        int res=0;

        for (Account a: getAs()){
            if(a instanceof CreditAccount){
                res++;
            }
        }
        return res;
    }

    public int getNumberOfSavingAccount(){
        int res=0;
        for(Account a: getAs()){
            if(a instanceof SavingAccount){
                res++;
            }
        }
        return res;
    }

    public ArrayList<Account> getAs() {
        return as;
    }
}
