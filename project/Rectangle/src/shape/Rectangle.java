package shape;

/**
 * @author:czfshine
 * @date:2017/11/21 10:05
 */

public class Rectangle {

    private double width ;
    private  double height;
    private String color="BLACK";

    public Rectangle(){
        this(1.0,1.0,"BLACK");
    }
    public Rectangle(double w,double h,String c){
        setWidth(w);
        setHeight(h);
        setColor(c);
    }


    public double getArea(){
        return getWidth() * getHeight();
    }

    public double getPerimeter(){
        return 2* getWidth() * getHeight();
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
