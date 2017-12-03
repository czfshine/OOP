package shape;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author:czfshine
 * @date:2017/11/21 10:10
 */

public class Utility {

    /**
     * (1) public static int compare(Rectangle rect1, Rectangle rect2)

     功能：

     如果rect1的面积比rect2的面积大，返回值为1

     如果rect1的面积比rect2的面积小，返回值为-1

     如果rect1的面积与rect2的面积相同，返回值为0
*/

    public static int compare(Rectangle rect1, Rectangle rect2){

        return (int) (rect1.getArea()-rect2.getArea());
    }



    /**
            **
     (2) public static void sort(Rectangle[] rectangles)

     功能：

     按照矩形的面积从大到小，对数组rectangles进行排序。



     */
    public static void sort(Rectangle[] rectangles){

        Arrays.sort(rectangles,(Rectangle a,Rectangle b)->{
            return compare(a,b);
        });
    }

    /**(3) public static void output(Rectangle[] rectangles)

     功能：

     按下标顺序依次输出数组rectangles中的所有矩形。

     每行输出一个矩形，矩形的输出格式是：[宽, 高] – 面积，均保留2位小数。
     */
    public static void output(Rectangle[] rectangles){
        for(int i=0;i<rectangles.length;i++){
            System.out.printf("[%.2f,%.2f]-%.2f\n",rectangles[i].getWidth(),rectangles[i].getHeight(),rectangles[i].getArea());
        }
    }

}
