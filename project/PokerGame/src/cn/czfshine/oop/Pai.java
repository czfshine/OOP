package cn.czfshine.oop;
import java.util.*;

/**
 * @author:czfshine
 * @date:2017/10/24 23:25
 */

public class Pai  {
        int num;
        int  huase;

    Pai(int i,int j){
        num=i;
        huase=j;
    }
    /**
     * TODO :FUCK JAVA 不支持重载操作符。
     */

    public final boolean LF(Pai u){
        return huase*1000+num >=u.huase*1000+u.num;
    }


}
