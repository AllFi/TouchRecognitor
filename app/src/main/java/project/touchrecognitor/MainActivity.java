package project.touchrecognitor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{

    Button create = null;
    EditText name = null;
    EditText count = null;
    CheckBox scrolls = null;
    CheckBox taps = null;
    CheckBox resizes = null;
    CheckBox twists = null;



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (EditText) findViewById(R.id.name);
        count = (EditText) findViewById(R.id.count);
        scrolls = (CheckBox) findViewById(R.id.scrolls);
        taps = (CheckBox) findViewById(R.id.taps);
        resizes = (CheckBox) findViewById(R.id.resizes);
        twists = (CheckBox) findViewById(R.id.twists);
        create = (Button) findViewById(R.id.create_button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().equals("Ваше имя")){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Введите ваше имя, пожалуйста!", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if ( Integer.parseInt(count.getText().toString()) <=  0 || Integer.parseInt(count.getText().toString()) > 100){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Введите корректное значение, пожалуйста!", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                Intent settings = getIntent();
                Intent intent = new Intent(MainActivity.this, RecognitorActivity.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("count", count.getText().toString());
                intent.putExtra("scrolls", scrolls.isChecked());
                intent.putExtra("taps", taps.isChecked());
                intent.putExtra("resizes", resizes.isChecked());
                intent.putExtra("twists", twists.isChecked());

                startActivity(intent);

            }
        });

    }
}