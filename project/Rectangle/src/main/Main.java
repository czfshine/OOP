package main;

import shape.Rectangle;
import shape.Utility;

/**
 * @author:czfshine
 * @date:2017/11/21 10:27
 */

public class Main {
    public static void main(String [] args){
        Rectangle[] res=new Rectangle[10];
        for(int i=0;i<10;i++){
            res[i]=new Rectangle(101-i*10,10-i+1,"RED");
        }

        Utility.output(res);
        Utility.sort(res);
        Utility.output(res);
    }
}
