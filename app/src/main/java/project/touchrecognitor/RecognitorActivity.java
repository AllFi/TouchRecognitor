package project.touchrecognitor;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecognitorActivity extends Activity implements View.OnTouchListener {

    private StringBuilder sb = new StringBuilder();
    private TextView tv;
    private int upPI = 0;
    private int downPI = 0;
    private boolean inTouch = false;
    private String result = "";
    private CSVFileGenerator csv;
    private Button bt = null;
    private String name = "";
    private int count = 10;
    private ArrayList<String> activeTypes = new ArrayList<String>();
    private int Counter =0;
    private int Type = 0;
    private boolean end = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognitor);
        tv = (TextView) findViewById(R.id.tv);
        tv.setTextSize(30);
        tv.setOnTouchListener(this);

        name = getIntent().getStringExtra("name");
        count = Integer.parseInt(getIntent().getStringExtra("count"));
        csv = new CSVFileGenerator(name);

        bt =(Button) findViewById(R.id.save_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                csv.writeFileSD();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        bt.setVisibility(View.INVISIBLE);

        boolean scrolls = getIntent().getBooleanExtra("scrolls", false);
        boolean taps = getIntent().getBooleanExtra("taps", false);
        boolean resizes = getIntent().getBooleanExtra("resizes", false);
        boolean twists = getIntent().getBooleanExtra("twists", false);

        if (scrolls){
            activeTypes.add("scrollUp");
            activeTypes.add("scrollDown");
            activeTypes.add("scrollLeft");
            activeTypes.add("scrollRight");
        }

        if (taps){
            activeTypes.add("tap");
        }

        if (resizes){
            activeTypes.add("pinch");
            activeTypes.add("stretch");
        }

        if (twists){
            activeTypes.add("clockwiseTwist");
            activeTypes.add("counterclockTwist");
        }

        sb.setLength(0);
        sb.append("Тип касаний: " + activeTypes.get(0) + "\r\n");
        sb.append("Осталось: " + String.valueOf(count));
        tv.setText(sb.toString());
    }


    private Motion motion = new Motion();

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        if (end){
            return false;
        }

        int actionMask = event.getActionMasked();
        // индекс касания
        int pointerIndex = event.getActionIndex();
        // число касаний
        int pointerCount = event.getPointerCount();


        switch (actionMask) {
            case MotionEvent.ACTION_DOWN: // первое касание
                inTouch = true;
            case MotionEvent.ACTION_POINTER_DOWN: // последующие касания
                downPI = pointerIndex;
                break;
            case MotionEvent.ACTION_UP: // прерывание последнего касания
                inTouch = false;

                if(!csv.addMotion(motion, activeTypes.get(Type))){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы ошиблись, пожалуйста повторите!!", Toast.LENGTH_SHORT);
                    toast.show();
                    Counter--;
                }

                Counter++;
                if (Counter == count){
                    if (Type < activeTypes.size() - 1) {
                        Type++;
                        Counter = 0;

                    } else{
                        end = true;
                        bt.setVisibility(View.VISIBLE);
                        sb.setLength(0);
                        sb.append("Пожалуйста сохраните свой результат!");
                        // событие
                        String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
                        int permsRequestCode = 200;
                        requestPermissions(perms, permsRequestCode);
                        break;
                    }
                }

                sb.setLength(0);
                sb.append("Тип касаний: " + activeTypes.get(Type) + "\r\n");
                sb.append("Осталось: " + String.valueOf(count - Counter));

                motion = new Motion();
            case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
                upPI = pointerIndex;
                break;
            case MotionEvent.ACTION_MOVE: // движение
                for (int i = 0; i < 10; i++) {
                    if (i < pointerCount) {
                        motion.addPoint(event.getPointerId(i), event.getX(i), event.getY(i),event.getSize(i),event.getEventTime());
                    }
                }
                break;
        }

        tv.setText(sb.toString());
        return true;
    }
}
