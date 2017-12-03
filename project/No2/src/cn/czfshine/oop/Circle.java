package cn.czfshine.oop;


import static java.lang.Math.abs;

/*
    @author:czfshine
    @date:2017/9/27 21:59    
*/public class Circle {

    public float x,y,r;

    Circle(float dx,float dy,float dr){
        x=dx;
        y=dy;
        r=dr;
    }

    int Check(Circle other){
        float d2=(x-other.x)*(x-other.x)+(y-other.y)*(y-other.y);

        float r_sum=r+other.r;
        float r_sub=abs(r-other.r);
        if(d2<r_sub){
            return -1;
        }else{
            if(d2>r_sum){
                return 0;
            }else{
                return 1;
            }
        }
    }
}
