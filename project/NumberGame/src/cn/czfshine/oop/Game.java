package cn.czfshine.oop;


import java.util.Random;

/**
    @author:czfshine
    @date:2017/9/30 21:35    
*/


public class Game {

    public enum Relust{
        LF,EQ,RF
    }

    public int ans;
    Game(){
        GetAns();
    }

    private void GetAns(){
        Random rand=new Random();
        ans=rand.nextInt(10);
        //System.out.println(ans);
    }

    public Relust Guest(int trynum){
        if(ans==trynum){
            return Relust.EQ;
        }else if(trynum>ans){
            return Relust.RF;
        }else{
            return Relust.LF;
        }
    }
}
