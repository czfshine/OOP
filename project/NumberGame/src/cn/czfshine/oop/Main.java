package cn.czfshine.oop;

import java.util.Scanner;

/**
 @author:czfshine
 @date:2017/9/30 21:35
 */
public class Main {

    public static void main(String[] args) {
	    Game numbergame =new Game();
        Game.Relust status= Game.Relust.LF;
        int ans;

        Scanner reader=new Scanner(System.in);

        int guestnum=3;
        System.out.printf("你还有%d次机会，请输入：",guestnum);
        while(guestnum>0 && status!= Game.Relust.EQ){

            while (true){
                try {
                    ans =reader.nextInt();
                    break;
                }catch (Exception e){
                    reader.nextLine();/*吃掉多余的*/
                    System.out.print("输入错误，请重新输入：");
                }

            }
            guestnum--;
            status=numbergame.Guest(ans);
            switch (status){
                case LF: if(guestnum==0){
                    System.out.printf("游戏结束，心疼你一秒，正确答案是%d",numbergame.ans);
                    return ;
                }
                    System.out.printf("偏小，你还有%d次机会，请重新输入：",guestnum); break;
                case EQ: System.out.println("答对了！棒棒哒！");break;
                case RF:
                    if(guestnum==0){
                        System.out.printf("游戏结束，正确答案是%d",numbergame.ans);
                        return ;
                    }
                    System.out.printf("偏大，你还有%d次机会，请重新输入：",guestnum);break;

            }
        }
    }
}
