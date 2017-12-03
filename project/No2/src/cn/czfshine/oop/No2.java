package cn.czfshine.oop;


import java.util.Scanner;

/*
    @author:czfshine
    @date:2017/9/27 21:40    
*/
public class No2 {
    public static void main(String[] args){
        Scanner input=new Scanner(System.in);

        float x,y,r;
        System.out.println("input the first Circle info:");
        x=input.nextFloat();
        y=input.nextFloat();
        r=input.nextFloat();

        Circle C1=new Circle(x,y,r);
        System.out.println("input the second Circle info:");
        x=input.nextFloat();
        y=input.nextFloat();
        r=input.nextFloat();

        Circle C2=new Circle(x,y,r);

        int flag=C1.Check(C2);
        switch (flag){
            case 1: System.out.printf("圆 (%.2f, %.2f) - %.2f 与 圆 (%.2f, %.2f) - %.2f 相交",C1.x,C1.y,C1.r,C2.x,C2.y,C2.r); break;
            case 0: System.out.printf("圆 (%.2f, %.2f) - %.2f 与 圆 (%.2f, %.2f) - %.2f 无关",C1.x,C1.y,C1.r,C2.x,C2.y,C2.r); break;
            case -1:System.out.printf("圆 (%.2f, %.2f) - %.2f 包含 圆 (%.2f, %.2f) - %.2f",C1.x,C1.y,C1.r,C2.x,C2.y,C2.r); break;
        }
    }
}
