package android.ryons.com.bwtiffimaging.imaging;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;

import android.ryons.com.bwtiffimaging.ContextWrapperInt;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Collection of utility functions used in this package.
 */
public class ImageUtils {

    private static final String TAG = "db.Util";

    private ImageUtils() {

    }
    

    public static void closeSilently(Closeable c) {

        if (c == null) return;
        try {
            c.close();
        } catch (Throwable t) {
            // do nothing
        }
    }


    // Returns Options that set the puregeable flag for Bitmap decode.
    public static BitmapFactory.Options createNativeAllocOptions() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inNativeAlloc = true;
        return options;
    }

    // Thong added for rotate
    public static Bitmap rotateImage(Bitmap src, float degree) {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }

    public static int getOrientationInDegree(Activity activity) {

        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        return degrees;
    }


    /** Create a File for saving an image */
    protected static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BWTiffImaging");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("BWTiffImaging", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        @SuppressLint("SimpleDateFormat") String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    public static String getImagePath(Uri uri){
        Cursor cursor = ContextWrapperInt.CONTEXT.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = ContextWrapperInt.CONTEXT.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    public static boolean blurCheck (Uri imageUri){

        boolean isBlurred = false;

        try {
            Bitmap image = MediaStore.Images.Media.getBitmap(ContextWrapperInt.CONTEXT.getContentResolver(), imageUri);

            int l = CvType.CV_8UC1; //8-bit grey scale image
            Mat matImage = new Mat();
            Utils.bitmapToMat(image, matImage);
            Mat matImageGrey = new Mat();
            Imgproc.cvtColor(matImage, matImageGrey, Imgproc.COLOR_BGR2GRAY);

            Bitmap destImage;
            destImage = Bitmap.createBitmap(image);
            Mat dst2 = new Mat();
            Utils.bitmapToMat(destImage, dst2);
            Mat laplacianImage = new Mat();
            dst2.convertTo(laplacianImage, l);
            Imgproc.Laplacian(matImageGrey, laplacianImage, CvType.CV_8U);
            Mat laplacianImage8bit = new Mat();
            laplacianImage.convertTo(laplacianImage8bit, l);

            Bitmap bmp = Bitmap.createBitmap(
                    laplacianImage8bit.cols(),
                    laplacianImage8bit.rows(),
                    Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(laplacianImage8bit, bmp);
            int[] pixels = new int[bmp.getHeight() * bmp.getWidth()];
            bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

            int maxLap = -16777216; // 16m
            for (int pixel : pixels) {
                if (pixel > maxLap)
                    maxLap = pixel;
            }

            int soglia = -12447983;
            if (maxLap <= soglia) {
                System.out.println("is blurry image - maxlap: " + maxLap);
                isBlurred = true;
            }
        } catch(Exception ex) {
            Log.e("ImageUtils: ",
                    "Error in blur detection.", ex);
        }
        return isBlurred;
    }

    public static boolean getBackCameraResolutionInMp()
    {
//        int noOfCameras = Camera.getNumberOfCameras();
//        float maxResolution = -1;
//        long pixelCount = -1;
//
//
//        for (int i = 0;i < noOfCameras;i++)
//        {
//            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//            Camera.getCameraInfo(i, cameraInfo);
//
//            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
//            {
//                Camera camera = Camera.open(i);
//                Camera.Parameters cameraParams = camera.getParameters();
//                for (int j = 0;j < cameraParams.getSupportedPictureSizes().size();j++)
//                {
//                    long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height; // Just changed i to j in this loop
//                    if (pixelCountTemp > pixelCount)
//                    {
//                        pixelCount = pixelCountTemp;
//                        maxResolution = ((float)pixelCountTemp) / (1024000.0f);
//                    }
//                }
//
//                camera.release();
//            }
//        }
//        return maxResolution < 10.0f;
        return false;
    }

    protected static void makeTempImageCopy(File originalPath, File tempPath) {
        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(originalPath).getChannel();
            destination = new FileOutputStream(tempPath).getChannel();
            destination.transferFrom(source, 0, source.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (destination != null) {
                try {
                    destination.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
