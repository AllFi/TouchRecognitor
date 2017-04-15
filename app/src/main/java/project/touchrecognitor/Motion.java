package project.touchrecognitor;

import android.util.Log;

import java.util.Vector;

/**
 * Created by админ on 22.03.2017.
 */

public class Motion {

    public Vector<Vector<Point>> Touchs;

    public Motion(){
        Touchs = new Vector<Vector<Point>>();
        for (int i =0; i<2; i++){
            Touchs.add(null);
        }
    }

    public void addPoint(int touchId, double X, double Y, double Size, long Time){
        if ( Touchs.get(touchId) == null){
            Touchs.set(touchId, new Vector<Point>());
            Touchs.get(touchId).add(new Point(X,Y,Size,Time));
        } else{
            Touchs.get(touchId).add(new Point(X,Y,Size,Time));
        }
        return;
    }

}
