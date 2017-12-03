package cn.czfshine.oop;

import java.util.InputMismatchException;
import java.util.Scanner;

/*
    @author:czfshine
    @date:2017/9/27 19:10
*/
public class No1 {
    public  Scanner input;

    public No1() {
        input = new Scanner(System.in);
        int in = 0;
        try{
            in = input.nextInt();
        }catch (InputMismatchException e){
            System.out.println("Input Mismatch!");
            return ;
        }catch (Exception e){
            System.out.println("This world is mad!");
            return ;
        }

        System.out.println(sum(in));

    }

    final private int sum(int innum){
        int a=0;
        a+=innum/100;
        innum%=100;
        a+=innum/10;
        innum%=10;
        return a+innum;
    }
    public static void main(String[] args) {
        new No1();
    }
}
