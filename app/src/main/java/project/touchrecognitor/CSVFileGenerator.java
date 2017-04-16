package project.touchrecognitor;

import android.os.Environment;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import static java.lang.Math.abs;

/**
 * Created by админ on 22.03.2017.
 */

public class CSVFileGenerator extends ContextCompat {
    public long MotionNomber;
    private String Result;
    public String LOG_TAG = "TouchRecogintor:";
    public String FILENAME_SD = "Touches.csv";
    public String DIR_SD = "TouchRecognitor";

    public CSVFileGenerator(String name){
        MotionNomber = 0;
        Result = "MotionNumber;TouchNumber;XCoordinate;YCoordinate;Size;Time;Type\n";
        name = name.replace(" ", "_");
        FILENAME_SD = name.toUpperCase() + ".csv";
    }

    public boolean checkType(Motion motion, String type){
        double XVec, YVec, XVec2 = 0, YVec2 = 0;
        int count = 0;
        if (motion.Touchs.get(1) == null){
            count = 1;
            if (motion.Touchs.get(0) == null) return false;
            int size = motion.Touchs.get(0).size();

            XVec = motion.Touchs.get(0).get(size - 1).X - motion.Touchs.get(0).get(0).X;
            YVec = motion.Touchs.get(0).get(size - 1).Y - motion.Touchs.get(0).get(0).Y;
        } else{
            count = 2;
            if (motion.Touchs.get(0) == null) return false;
            int size1 = motion.Touchs.get(0).size();
            int size2 = motion.Touchs.get(1).size();

            XVec = motion.Touchs.get(0).get(size1 - 1).X - motion.Touchs.get(0).get(0).X;
            YVec = motion.Touchs.get(0).get(size1 - 1).Y - motion.Touchs.get(0).get(0).Y;
            XVec2 = motion.Touchs.get(1).get(size2 - 1).X - motion.Touchs.get(1).get(0).X;
            YVec2 = motion.Touchs.get(1).get(size2 - 1).Y - motion.Touchs.get(1).get(0).Y;
        }
        if (count == 1){
            if (abs(XVec) + abs(YVec) < 100){
                return type == "TP";
            }
            if (motion.Touchs.get(0).size() < 6) return false;
            if (abs(YVec)>abs(XVec)){
                if (YVec < 0){
                    return type =="DU";
                } else{
                    return type =="UD";
                }
            } else{
                if (XVec > 0){
                    return type =="LR";
                } else{
                    return type =="RL";
                }
            }
        } else{
            if (motion.Touchs.get(0).size() < 6 && motion.Touchs.get(1).size() < 6 ) return false;
            if (motion.Touchs.get(0).get(0).Y > motion.Touchs.get(1).get(0).Y ){
                XVec = XVec;
                YVec = YVec;
            } else{
                XVec = XVec2;
                YVec = YVec2;
            }
            if (abs(YVec)>abs(XVec)){
                if (YVec > 0){
                    return type =="ZO";
                } else{
                    return type =="ZI";
                }
            } else{
                if (XVec < 0){
                    return type =="TC";
                } else{
                    return type =="TCC";
                }
            }
        }
    }

    public boolean addMotion(Motion motion, String type){
        boolean ctype = checkType(motion, type);
        if (!ctype) return false;
        for (int i=0; i<2; i++){
            if (motion.Touchs.get(i) != null){
                for (int j=0; j < motion.Touchs.get(i).size(); j++){
                    Result += String.valueOf(MotionNomber) + ";" + String.valueOf(i) +  ";" + motion.Touchs.get(i).get(j).ToString() + ";" + type + "\n";
                }
            } else{
                break;
            }
        }
        MotionNomber++;
        return true;
    }

    public void writeFileSD() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write(Result);
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
