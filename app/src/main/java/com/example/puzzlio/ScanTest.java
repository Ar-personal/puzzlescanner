package com.example.puzzlio;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ScanTest extends AppCompatActivity {


    private static final String TAG = ScanTest.class.getSimpleName();
    public static final String TESS_DATA = "/tessdata";
    private TessBaseAPI tessBaseAPI;
    private Uri outputFileDir;
    private String DATA_PATH, DATA_PATH_LOCAL;
    private String mCurrentPhotoPath;
    private ImageProcessing imageProcessing;
    private Bitmap bitmap, finalbmp;
    private List<Mat> grids;
    private Bitmap gridBitmaps[][] = new Bitmap[9][9];
    private String [][] scannedVals = new String[9][9];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scantest);

        Context mContext = getApplicationContext();
        DATA_PATH = mContext.getExternalFilesDir(null).toString() + "/Tess";
        DATA_PATH_LOCAL = mContext.getFilesDir() + "/tesseract";

//        setContentView(R.layout.capture);

        //test if open cv loads correctly
        if(OpenCVLoader.initDebug()){
            Toast.makeText(this, "loaded", Toast.LENGTH_SHORT).show();
        }else{
            Log.d("ok", "open cv fault");
        }

        TextView textView = findViewById(R.id.ocrtext);
        Button button1 = findViewById(R.id.processbutton);

        ImageView test = findViewById(R.id.imageView4);

        //read image form file
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sudokuexample).copy(Bitmap.Config.ARGB_8888, true);


        imageProcessing = new ImageProcessing(ScanTest.this);
        //pass bitmap to class
        imageProcessing.setBitmap(bitmap);

        //returns a bitmap to display to imageview via button press
        imageProcessing.img();


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test.setImageBitmap(bitmap);
                test.setImageBitmap(finalbmp);
                textView.setText("");

                //pass image to tesseract and return ocr text


                for(int i = 0; i < 9; i++){
                    for(int j = 0; j < 9; j++){
                        String res = getText(gridBitmaps[i][j]);
                        scannedVals[i][j] = res;
                        textView.append(res + " ");
                    }
                }


//                textView.setText(res);
            }
        });


        //temp code
        Button createTest = findViewById(R.id.createtest);
        createTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SudokuCreator.class);
                intent.putExtra("valuesScanned", scannedVals);
                intent.putExtra("type", 2);
                intent.putExtra("title", "scanned sudoku");
                intent.putExtra("dims", new int[]{9, 9});
                intent.putExtra("scanned", true);
                startActivity(intent);
                finish();
            }
        });

    }


    public void setBitmaps(Mat m){
        finalbmp = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(m, finalbmp);
    }

    public void writeMats(List<Mat> mats){

        //currently loops through hardcoded sudoku grid
        int m = 0;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                try {
                    gridBitmaps[i][j] = Bitmap.createBitmap(mats.get(m).cols(), mats.get(m).rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(mats.get(m), gridBitmaps[i][j]);


                }catch (CvException o) {
                    o.printStackTrace();
                }

                mats.get(m).release();


                File sd = new File(getExternalFilesDir("/grids").toString() + "/" + m + ".png");
                try {
                    sd.createNewFile();
                    FileOutputStream out = new FileOutputStream(sd);
                    gridBitmaps[i][j].compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                m++;
            }
         }
    }

    private String getText(Bitmap bitmap){
        try{
            tessBaseAPI = new TessBaseAPI();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        String dataPath = getExternalFilesDir("/").getPath() + "/";
        System.out.println(dataPath);
        tessBaseAPI.init(dataPath, "eng");
        tessBaseAPI.setImage(bitmap);
        String retStr = "No result";
        try{
            retStr = tessBaseAPI.getUTF8Text();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        tessBaseAPI.end();
        return retStr;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1024) {
            if (resultCode == Activity.RESULT_OK) {
                prepareTessData();
//                startOCR(outputFileDir);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Result canceled.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Activity result failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void prepareTessData(){
        try{
            File dir = getExternalFilesDir(TESS_DATA);
            if(!dir.exists()){
                if (!dir.mkdir()) {
                    Toast.makeText(getApplicationContext(), "The folder " + dir.getPath() + "was not created", Toast.LENGTH_SHORT).show();
                }
            }
            String fileList[] = getAssets().list("");
            for(String fileName : fileList){
                String pathToDataFile = dir + "/" + fileName;
                if(!(new File(pathToDataFile)).exists()){
                    InputStream in = getAssets().open(fileName);
                    OutputStream out = new FileOutputStream(pathToDataFile);
                    byte [] buff = new byte[1024];
                    int len ;
                    while(( len = in.read(buff)) > 0){
                        out.write(buff,0,len);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
