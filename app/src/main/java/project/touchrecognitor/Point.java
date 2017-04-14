package project.touchrecognitor;

/**
 * Created by админ on 22.03.2017.
 */

public class Point {
    public double X;
    public double Y;
    public double Size;
    public long Time;

    public Point(double x, double y, double size, long time){
        X = x;
        Y = y;
        Size = size;
        Time = time;
    }

    public String ToString(){
        return String.valueOf(X) + ";" + String.valueOf(Y)+ ";" + String.valueOf(Size)+ ";" + String.valueOf(Time);
    }

}
