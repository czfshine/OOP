package ui;

import bank.Account;
import manager.AccountManager;

import java.util.Scanner;

/**
 * @author:czfshine
 * @date:2017/11/21 23:19
 */

public class BankUI {

    private AccountManager am=new AccountManager();
    private Scanner stdin=new Scanner(System.in);
    public void showMainMenu(){
        System.out.println("1. 增加账户\n" +
                "\n" +
                "2. 删除账户\n" +
                "\n" +
                "3. 存款操作\n" +
                "\n" +
                "4. 取款操作\n" +
                "\n" +
                "5. 数据统计\n" +
                "\n" +
                "6. 账户列表\n" +
                "\n" +
                "0. 退出");
    }

    public void addAccount(){
        String id,name;
        System.out.println("id?:");
        id=stdin.nextLine();
        System.out.println("name?:");
        name=stdin.nextLine();
        if(am.addAccount(new Account(id,name,0.0))){
            System.out.println("success");
        }else{
            System.out.println("fail");
        };
    }

    public void removeAccount(){
        String id;
        System.out.println("id?:");
        id=stdin.nextLine();

        Account a=am.getAccount(id);
        if(a==null){
            System.out.println("can't find");
            return ;
        }
        System.out.println(a);
        System.out.println("del it?:[y\\N]");

        String op;
        op=stdin.nextLine();

        if(op=="y" ||op=="Y"){
            if(am.removeAccount(a)){
                System.out.println("del success");
            }else{
                System.out.println("del fail");
            }
        }

    }

    public void in(){

        String id;
        System.out.println("id?:");
        id=stdin.nextLine();

        Account a=am.getAccount(id);
        if(a==null){
            System.out.println("can't find");
            return ;
        }
        System.out.println(a);

        System.out.println("how many?:");

        double i=stdin.nextDouble();
        a.desopit(i);
        System.out.println(a);
    }

    public void out(){
        String id;
        System.out.println("id?:");
        id=stdin.nextLine();

        Account a=am.getAccount(id);
        if(a==null){
            System.out.println("can't find");
            return ;
        }
        System.out.println(a);

        System.out.println("how many?:");

        double i=stdin.nextDouble();
        if(a.withdraw(i)){
            System.out.println("del success");
        }else{
            System.out.println("del fail");
        }
        System.out.println(a);


    }

    public void print(){
        System.out.println("data:");
        System.out.println("num of acc");
        System.out.println(am.getNumberOfAccount());
        System.out.println("num of crd");
        System.out.println(am.getNumberOfCreditAccount());
        System.out.println("num of sav");
        System.out.println(am.getNumberOfSavingAccount());
        System.out.println("total money");
        System.out.println(am.getTotalBalance());
        System.out.println("all tozhi");
        System.out.println(am.getTotalOverdraft());
    }

    public void printAcc(){
        for(Account a :am.getAs()){
            System.out.println(a);
        }
    }
    public  void start(String [] args){
        while(true) {
            showMainMenu();

            int op;

            op = stdin.nextInt();
            stdin.nextLine();
            switch (op) {
                case 1:
                    addAccount();
                    break;
                case 2:
                    removeAccount();
                    break;
                case 3: in();
                    break;
                case 4: out();
                    break;
                case 5:print();
                    break;
                case 6:printAcc();
                    break;
                case 0:System.exit(0);
                    break;
            }
        }
    }
}
