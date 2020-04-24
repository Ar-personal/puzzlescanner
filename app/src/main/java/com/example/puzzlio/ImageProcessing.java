package com.example.puzzlio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.videoio.VideoCapture;



import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.icu.lang.UProperty.MATH;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_NONE;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.INTER_CUBIC;
import static org.opencv.imgproc.Imgproc.LINE_AA;
import static org.opencv.imgproc.Imgproc.MORPH_RECT;
import static org.opencv.imgproc.Imgproc.RETR_CCOMP;
import static org.opencv.imgproc.Imgproc.RETR_TREE;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.THRESH_OTSU;
import static org.opencv.imgproc.Imgproc.adaptiveThreshold;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.erode;
import static org.opencv.imgproc.Imgproc.getStructuringElement;
import static org.opencv.imgproc.Imgproc.resize;
import static org.opencv.imgproc.Imgproc.threshold;

public class ImageProcessing{

    private MainActivity mainActivity;

    private Mat grayMat, m, largestMat;
    private Bitmap bitmap, finalbmp;
    private double crosswordContourIdx;

    public ImageProcessing(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void img(){

        m = new Mat();

        Utils.bitmapToMat(bitmap, m);

        Size mSize = m.size();
        double factor = Math.min(1 , 1024.0 / mSize.width);

        Size reSize = new Size(factor * mSize.width, factor * mSize.height);
//        resize(m, m, new Size(mSize.width * 10, mSize.height * 10), INTER_CUBIC);

        Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2GRAY);
        Core.bitwise_not(m, m);

        grayMat = m.clone();
        Mat cannyEdges = new Mat();
        Mat lines= new Mat();

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(5, 5));
        Mat kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(3, 3));

//        resize(m, m, new Size(640, 480), 0, 0, INTER_CUBIC);


        Imgproc.Canny(grayMat, cannyEdges, 50, 300);

        Imgproc.dilate(cannyEdges, cannyEdges, kernel);
        Imgproc.erode(cannyEdges, cannyEdges, kernel2);

        //firstcontours
        Mat cropped = cropToLargestContour(cannyEdges);

        //second
        Mat cropped2 = cropToSmallestContour(cropped);

        Mat cropTest = cropTest(cropped);

//        Imgproc.HoughLinesP(cropped, lines, 1, Math.PI/180, 50);
//
//        Mat houghLines = new Mat();
//        houghLines.create(cropped.rows(), cropped.cols(), CvType.CV_8UC1);
//
//
//        for (int i = 0; i < lines.cols(); i++)
//        {
//            double rho = lines.get(i, 0)[0],
//                    theta = lines.get(i, 0)[1];
//            double a = Math.cos(theta), b = Math.sin(theta);
//            double x0 = a*rho, y0 = b*rho;
//            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
//            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
//
//
//            Imgproc.line(houghLines, pt1, pt2, new Scalar(0, 0, 255), 3);
//        }

//       Toast.makeText(this, "" + lines.cols(), Toast.LENGTH_SHORT).show();

        //bitmap creation and matToBitmap need to use same mat, use cropped for greyscale cropped image, without extra processing

        //set the bitmap to display image
        resize(cropped, cropped, new Size(130, 130));
        mainActivity.setBitmaps(cropTest);
    }

    public Mat cropToLargestContour(Mat mat){
        Mat hierachy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(mat, contours, hierachy, RETR_TREE, CHAIN_APPROX_SIMPLE);
        System.out.println("contours " + contours.size());

        double largest_area =0;
        int largest_contour_index = 0;

        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            double contourArea = Imgproc.contourArea(contours.get(contourIdx));
            System.out.println("contours " + contourArea);
            if (contourArea > largest_area) {
                largest_area = contourArea;
                largest_contour_index = contourIdx;
            }
        }
//        Imgproc.drawContours(mat, contours, largest_contour_index, new Scalar(0, 255, 255), 3);
        crosswordContourIdx = Imgproc.contourArea(contours.get(largest_contour_index));
        Rect rect = Imgproc.boundingRect(contours.get(largest_contour_index));
        Mat crop =  mat.submat(rect); //use m.submat for image without lines
        largestMat = crop;
        return crop;
    }


    public Mat cropToSmallestContour(Mat mat) {
        Mat hierachy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(mat, contours, hierachy, RETR_TREE, CHAIN_APPROX_SIMPLE);

        double smallest_area = 0;
        int smallest_contour_index = 0;
        int minContourSize = 1;

        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            double contourArea = Imgproc.contourArea(contours.get(contourIdx));
            if (contourArea < smallest_area && contourArea > minContourSize) {
                smallest_area = contourArea;
                smallest_contour_index = contourIdx;
            }
        }

        Rect rect = Imgproc.boundingRect(contours.get(smallest_contour_index));
        Mat crop =  mat.submat(rect); //use m.submat for image without lines

        return crop;
    }


    public Mat cropTest(Mat mat){
        Mat hierachy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(mat, contours, hierachy, RETR_TREE, CHAIN_APPROX_SIMPLE);
        List<MatOfPoint> filteredContours = new ArrayList<>();
        double sizeLower = (crosswordContourIdx / 1000) * 8;
        double sizeHigher = (crosswordContourIdx /1000) * 12;

        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            if(Imgproc.contourArea(contours.get(contourIdx)) < sizeLower || Imgproc.contourArea(contours.get(contourIdx)) > sizeHigher) {
                continue;
            }else{
                filteredContours.add(contours.get(contourIdx));
            }
        }

        System.out.println("lower size = " + sizeLower + " " + "upper = " + " " + sizeHigher + " dims");
        Rect rect = Imgproc.boundingRect(filteredContours.get(10));
        System.out.println("largest contour:" + crosswordContourIdx + " " + "dims: " + Imgproc.contourArea(filteredContours.get(9)) + " " + "length of list: " + filteredContours.size());
        //110
        //13466
        Mat crop =  mat.submat(rect); //use m.submat for image without lines
        extractGrids(largestMat, crop);
        return crop;
    }

    public void extractGrids(Mat grid, Mat crop){
        Mat m;
        System.out.println("dims " + grid.size() + " cropsize " + crop.size());
        int x = 0, y = 0;
        Rect rect = new Rect(x,  y, crop.width(), crop.height());
        List<Mat> grids = new ArrayList<>();

        int width = grid.width() / 10;
        int height = grid.height() / 10;

        System.out.println("iterate: " + width + " " + height);

        for(int i = 0; i < height; i++) {
            for (int o = 0; o < width; o++) {
                m = grid.submat(rect);
                grids.add(m);

                rect.x += width;
            }
            rect.x = 0;
            rect.y += height;
        }


//        int yDif = crop.height();
//            do {
//                if(rect.x + crop.width() >= grid.width()){
//                    int temp;
//                    temp = ((rect.x + crop.width() - grid.width()));
//                    rect.width = crop.width() - temp;
//                    m = grid.submat(rect);
//                    grids.add(m);
//
//                    rect.width = crop.width();
//
//                    if(rect.y + crop.height() >= grid.height()){
//                        yDif = ((rect.y + crop.height() - grid.height()));
//                        rect.height = crop.height() - yDif;
//                    }else {
//                        rect.y += crop.height();
//                        rect.x = 0;
//                    }
//                }else{
//                    m = grid.submat(rect);
//                    grids.add(m);
//                    rect.x += crop.width();
//                }
//
//                if(rect.x + crop.width() >= grid.height() && rect.y + crop.height() >= grid.height())
//                    break;
//
//
//            }
//            while (yDif == crop.height());

            mainActivity.writeMats(grids);


    }


    public Mat drawContours(Mat mat){
        Mat hierachy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(mat, contours, hierachy, RETR_TREE, CHAIN_APPROX_SIMPLE);



        for (int i = 0; i < contours.size(); i++) {
                Imgproc.drawContours(mat, contours, i, new Scalar(255, 0, 255), 3);
        }

        return mat;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getFinalbmp() {
        return finalbmp;
    }

    public void setFinalbmp(Bitmap finalbmp) {
        this.finalbmp = finalbmp;
    }
}
