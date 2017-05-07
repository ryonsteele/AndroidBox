package android.ryons.com.bwtiffimaging.imaging;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import android.ryons.com.bwtiffimaging.R;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * The activity can crop specific region of interest from an image.
 */
public class CropImage extends MonitoredActivity {

    final int IMAGE_MAX_SIZE = 1024;

    private static final String TAG                    = "CropImage";
    public static final  String IMAGE_PATH             = "image-path";
    public static final  String SCALE                  = "scale";
    public static final  String ORIENTATION_IN_DEGREES = "orientation_in_degrees";
    public static final  String ASPECT_X               = "aspectX";
    public static final  String ASPECT_Y               = "aspectY";
    public static final  String OUTPUT_X               = "outputX";
    public static final  String OUTPUT_Y               = "outputY";
    public static final  String SCALE_UP_IF_NEEDED     = "scaleUpIfNeeded";
    public static final  String CIRCLE_CROP            = "circleCrop";
    public static final  String RETURN_DATA            = "return-data";
    public static final  String RETURN_DATA_AS_BITMAP  = "data";
    public static final  String ACTION_INLINE_DATA     = "inline-data";

    // These are various options can be specified in the intent.
    private       Bitmap.CompressFormat mOutputFormat    = Bitmap.CompressFormat.JPEG;
    private       Uri                   mSaveUri         = null;
    private       boolean               mDoFaceDetection = true;
    private       boolean               mCircleCrop      = false;
    private final Handler               mHandler         = new Handler();

    private int             mAspectX;
    private int             mAspectY;
    private int             mOutputX;
    private int             mOutputY;
    private boolean         mScale;
    private CropImageView mImageView;
    private ContentResolver mContentResolver;
    private Bitmap          mBitmap;
    private String          mImagePath;

    boolean       mWaitingToPick; // Whether we are wait the user to pick a face.
    boolean       mSaving;  // Whether the "save" button is already clicked.
    HighlightView mCrop;

    // These options specifiy the output image size and whether we should
    // scale the output to fit it (or just crop it).
    private boolean mScaleUp = true;

    private final BitmapManager.ThreadSet mDecodingThreads =
            new BitmapManager.ThreadSet();

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        mContentResolver = getContentResolver();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cropimage);

        mImageView = (CropImageView) findViewById(R.id.image);
        Log.w("myApp", "mimageview is initialized ");


        runOnUiThread(new Runnable() {

            public void run() {

                Toast.makeText(CropImage.this, "Hit Save after your adjustments or hit use original if you dont want to crop or make any adjustment", Toast.LENGTH_LONG).show();
            }
        });




        // showStorageToast(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            if (extras.getString(CIRCLE_CROP) != null) {

                mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

                mCircleCrop = true;
                mAspectX = 1;
                mAspectY = 1;
            }

            mImagePath = extras.getString(IMAGE_PATH);

            mSaveUri = getImageUri(mImagePath);
            mBitmap = getBitmap(mImagePath);
            mBitmap = ImageUtils.rotateImage(mBitmap, 90);

            if (extras.containsKey(ASPECT_X) && extras.get(ASPECT_X) instanceof Integer) {

                mAspectX = extras.getInt(ASPECT_X);
            } else {

                throw new IllegalArgumentException("aspect_x must be integer");
            }
            if (extras.containsKey(ASPECT_Y) && extras.get(ASPECT_Y) instanceof Integer) {

                mAspectY = extras.getInt(ASPECT_Y);
            } else {

                throw new IllegalArgumentException("aspect_y must be integer");
            }
            mOutputX = extras.getInt(OUTPUT_X);//if OUTPUT_X is not mentioned moutputx will be =0
            mOutputY = extras.getInt(OUTPUT_Y);
            mScale = extras.getBoolean(SCALE, true);
            mScaleUp = extras.getBoolean(SCALE_UP_IF_NEEDED, true);
            Log.w("myApp",Integer.toString(mOutputX));

        }


        if (mBitmap == null) {

            Log.d(TAG, "finish!!!");
            finish();
            return;
        }

        // Make UI fullscreen.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewById(R.id.discard).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
        //added by minesh to use the original image if the user doesnt want to crop/rotate

        findViewById(R.id.use_original).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        try {
                            onUseOriginalClicked();
                        } catch (Exception e) {
                            finish();
                        }
                    }
                });

        findViewById(R.id.save).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        try {
                            onSaveClicked();
                        } catch (Exception e) {
                            finish();
                        }
                    }
                });
        findViewById(R.id.rotateLeft).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        mBitmap = ImageUtils.rotateImage(mBitmap, -90);
                        RotateBitmap rotateBitmap = new RotateBitmap(mBitmap);
                        mImageView.setImageRotateBitmapResetBase(rotateBitmap, true);
                        makeDefault();
                    }
                });

        findViewById(R.id.rotateRight).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        mBitmap = ImageUtils.rotateImage(mBitmap, 90);
                        RotateBitmap rotateBitmap = new RotateBitmap(mBitmap);
                        mImageView.setImageRotateBitmapResetBase(rotateBitmap, true);
                        makeDefault();
                    }
                });
        //startFaceDetection(); //
        mImageView.setImageBitmapResetBase(mBitmap, true); //

        // Log.w("myApp", "gonna call makeDefault() ");
        makeDefault();

        Log.w("myApp", " makeDefault() done ");

    }

    private Uri getImageUri(String path) {

        return Uri.fromFile(new File(path));
    }

    private Bitmap getBitmap(String path) {

        Uri uri = getImageUri(path);
        InputStream in = null;
        int scaledWidth = 1728;
        int scaledHeight = 2200;

        try {
            in = mContentResolver.openInputStream(uri);

            //Decode image size
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//
//            BitmapFactory.decodeStream(in, null, o);
//            in.close();
//
//            int scale = 1;
//            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
//                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
//            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
           // o2.inSampleSize = scale;
            in = mContentResolver.openInputStream(uri);
            Bitmap b = BitmapFactory.decodeStream(in, null, o2);
            in.close();

            int height = b.getHeight();
            int width = b.getWidth();
            scaledHeight = scaledWidth * height/width;
            b = Bitmap.createScaledBitmap(b, scaledWidth, scaledHeight, false);

            return b;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "file " + path + " not found");
        } catch (IOException e) {
            Log.e(TAG, "file " + path + " not found");
        }
        return null;
    }


    private void onSaveClicked() throws Exception {
        // TODO this code needs to change to use the decode/crop/encode single
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (mSaving) return;

        if (mCrop == null) {

            return;
        }

        mSaving = true;

        //   Rect r = mCrop.getCropRect();
        final float[] trapezoid = mCrop.getTrapezoid();
        Log.w("myApp", "onsaveclicekd, trap[0] is "+trapezoid[0]);
        Log.w("myApp", "onsaveclicekd, trap[1] is "+trapezoid[1]);

        Log.w("myApp", "onsaveclicekd, trap[2] is "+trapezoid[2]);

        Log.w("myApp", "onsaveclicekd, trap[3] is "+trapezoid[3]);

        Log.w("myApp", "onsaveclicekd, trap[4] is "+trapezoid[4]);

        Log.w("myApp", "onsaveclicekd, trap[5] is "+trapezoid[5]);

        Log.w("myApp", "onsaveclicekd, trap[6] is "+trapezoid[6]);
        Log.w("myApp", "onsaveclicekd, trap[7] is "+trapezoid[7]);



        /// refer this for perspective correction
        //find the bounding rectangle of the quadilateral
        //new image to whiche perspective corrected matrix to be plotted will be made in this size
        final RectF perspectiveCorrectedBoundingRect = new RectF(mCrop.getPerspectiveCorrectedBoundingRect());


        //dimension of the new image
        int result_width =(int)perspectiveCorrectedBoundingRect.width();
        int result_height =(int)perspectiveCorrectedBoundingRect.height();


        Log.w("myApp", "bounding rect width is "+result_width);
        Log.w("myApp", "bounding rect height "+ result_height   );

        Mat inputMat=new Mat(mBitmap.getHeight(), mBitmap.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(mBitmap, inputMat);
        final Mat outputMat = new Mat(result_width, result_height, CvType.CV_8UC4);


        //the 4 points of the quad,
        Point ocvPIn1 = new Point((int)trapezoid[0], (int)trapezoid[1]);//left top
        Point ocvPIn2 = new Point((int)trapezoid[6], (int)trapezoid[7]);//left bottom
        Point ocvPIn3 = new Point((int)trapezoid[4],(int)trapezoid[5]); //bottom right
        Point ocvPIn4 = new Point((int)trapezoid[2], (int)trapezoid[3]);//right top

        List<Point> source = new ArrayList<Point>();
        source.add(ocvPIn1);
        source.add(ocvPIn2);
        source.add(ocvPIn3);
        source.add(ocvPIn4);

        Mat startM = Converters.vector_Point2f_to_Mat(source);


        //points in the destination imafge
        Point ocvPOut1 = new Point(0, 0);// lfet top
        Point ocvPOut2 = new Point(0, result_height);//left bottom
        Point ocvPOut3 = new Point(result_width, result_height); //bottom right
        Point ocvPOut4 = new Point(result_width, 0);//right top


        List<Point> dest = new ArrayList<Point>();
        dest.add(ocvPOut1);
        dest.add(ocvPOut2);
        dest.add(ocvPOut3);
        dest.add(ocvPOut4);
        Mat endM = Converters.vector_Point2f_to_Mat(dest);


        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);
        Imgproc.warpPerspective(inputMat,
                outputMat,
                perspectiveTransform,
                new Size(result_width, result_height),
                Imgproc.INTER_CUBIC);


        Imgcodecs.imwrite(mImagePath, outputMat);

        Intent intent = new Intent();
        intent.putExtra(IMAGE_PATH, mImagePath);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void saveOutput(Bitmap croppedImage) {

        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = mContentResolver.openOutputStream(mSaveUri);
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 100, outputStream);
                }
            } catch (IOException ex) {

                Log.e(TAG, "Cannot open file: " + mSaveUri, ex);
                setResult(RESULT_CANCELED);
                finish();
                return;
            } finally {

                ImageUtils.closeSilently(outputStream);
            }

            Bundle extras = new Bundle();
            Intent intent = new Intent(mSaveUri.toString());
            intent.putExtras(extras);
            intent.putExtra(IMAGE_PATH, mImagePath);
            Log.w("myApp","when cropped location is" +mImagePath);
            intent.putExtra(ORIENTATION_IN_DEGREES, ImageUtils.getOrientationInDegree(this));
            setResult(RESULT_OK, intent);
        } else {

            Log.e(TAG, "not defined image url");
        }
        croppedImage.recycle();
        finish();
    }

    private void onUseOriginalClicked()//what to do when uuser dont want to do any edit on the image
    {


//        earlier one -- without saving gray image again
        //when the original image has to be used nothing much needs to be done;
        //just return with result_ok, we can continue with the image at the tempfilelocation

        Mat inputMat=new Mat(mBitmap.getHeight(), mBitmap.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(mBitmap, inputMat);
        Imgcodecs.imwrite(mImagePath, inputMat);

        Intent intent = new Intent();
        intent.putExtra(IMAGE_PATH, mImagePath);
        Log.w("myApp", "when not cropped location is" + mImagePath);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BitmapManager.instance().cancelThreadDecoding(mDecodingThreads);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBitmap != null) {
            mBitmap.recycle();
        }
    }

    private void makeDefault() {

        // finding the largest rect in the given image


        //Mat grayImage= Imgcodecs.imread(IMAGE_PATH, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        Mat imgSource=new Mat();
        Utils.bitmapToMat(mBitmap, imgSource);
        //  Utils.bitmapToMat(bmp32, imgMAT);
        Imgproc.cvtColor(imgSource, imgSource, Imgproc.COLOR_BGR2GRAY);


        //Mat imgSource = Imgcodecs.imread(mImagePath,Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        Log.w("myApp", "image path from isnde makedefault() is " + mImagePath);


        int matwidth=imgSource.width();
        int matheight=imgSource.height();

        Log.w("myApp", "mat image width, from makedefault() is "+matwidth);
        Log.w("myApp", "mat image height from, makedefault() is "+matheight);


        Mat imageBin=new Mat();

        double threshold=Imgproc.threshold(imgSource, imageBin, 0, 255, Imgproc.THRESH_OTSU);
        Log.w("myApp", "otsu threshold is " + threshold);

        //for canny higher threshold is chosen as otsus threshold and lower threshold is half of the otsu threshold value
        Imgproc.Canny(imgSource.clone(), imgSource, threshold * 0.5, threshold);
        Imgproc.GaussianBlur(imgSource, imgSource, new Size(3, 3), 3);
        // find the contours
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        //MatVector contours = new MatVector();

        Imgproc.findContours(imgSource, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        double maxArea = -1;
        MatOfPoint temp_contour = contours.get(0); // the largest is at the
        // index 0 for starting
        // point
        MatOfPoint2f approxCurve = new MatOfPoint2f();

        for (int idx = 0; idx < contours.size(); idx++) {
            temp_contour = contours.get(idx);
            double contourarea = Imgproc.contourArea(temp_contour);
            // compare this contour to the previous largest contour found
            if (contourarea > maxArea) {
                // check if this contour is a square
                MatOfPoint2f new_mat = new MatOfPoint2f(temp_contour.toArray());
                int contourSize = (int) temp_contour.total();
                MatOfPoint2f approxCurve_temp = new MatOfPoint2f();
                Imgproc.approxPolyDP(new_mat, approxCurve_temp, contourSize * 0.05, true);
                if (approxCurve_temp.total() == 4) {
                    maxArea = contourarea;
                    approxCurve = approxCurve_temp;
                }
            }
        }
        double[] temp_double;
        temp_double = approxCurve.get(0, 0);
        Point p1 = new Point(temp_double[0], temp_double[1]);
        // Core.circle(imgSource,p1,55,new Scalar(0,0,255));
        // Imgproc.warpAffine(sourceImage, dummy, rotImage,sourceImage.size());
        temp_double = approxCurve.get(1, 0);
        Point p2 = new Point(temp_double[0], temp_double[1]);
        // Core.circle(imgSource,p2,150,new Scalar(255,255,255));
        temp_double = approxCurve.get(2, 0);
        Point p3 = new Point(temp_double[0], temp_double[1]);
        // Core.circle(imgSource,p3,200,new Scalar(255,0,0));
        temp_double = approxCurve.get(3, 0);
        Point p4 = new Point(temp_double[0], temp_double[1]);
        // Core.circle(imgSource,p4,100,new Scalar(0,0,255));
        ArrayList<Point> source = new ArrayList<Point>();
        ArrayList<Point> topPoints = new ArrayList<Point>();
        ArrayList<Point> bottomPoints = new ArrayList<Point>();
        ArrayList<Point> sortedPoints = new ArrayList<Point>();

        source.add(p1);
        source.add(p2);
        source.add(p3);
        source.add(p4);



        Collections.sort(source, new Comparator<Point>() {

            public int compare(Point o1, Point o2) {
                return Double.compare(o1.y, o2.y);
            }
        });

        topPoints.add(source.get(0));
        topPoints.add(source.get(1));

        Collections.sort(topPoints, new Comparator<Point>() {

            public int compare(Point o1, Point o2) {
                return Double.compare(o1.x, o2.x);
            }
        });



        bottomPoints.add(source.get(2));
        bottomPoints.add(source.get(3));

        Collections.sort(bottomPoints, new Comparator<Point>() {

            public int compare(Point o1, Point o2) {
                return Double.compare(o1.x, o2.x);
            }
        });

        sortedPoints.add(topPoints.get(0));//top left
        sortedPoints.add(bottomPoints.get(0));//bottom left
        sortedPoints.add(bottomPoints.get(1));//bottom right
        sortedPoints.add(topPoints.get(1));//top right

        for(Point pnt : sortedPoints){
            System.out.println("Point: "+pnt.toString());
        }

        boolean defaultTrap = false;

        if(isPointInCircle(sortedPoints.get(0).x, sortedPoints.get(0).y, 200.0, sortedPoints.get(1).x, sortedPoints.get(1).y)
                || isPointInCircle(sortedPoints.get(1).x, sortedPoints.get(1).y, 200.0, sortedPoints.get(2).x, sortedPoints.get(2).y)
                || isPointInCircle(sortedPoints.get(3).x, sortedPoints.get(3).y, 200.0, sortedPoints.get(0).x, sortedPoints.get(0).y)
                || isPointInCircle(sortedPoints.get(2).x, sortedPoints.get(2).y, 200.0, sortedPoints.get(0).x, sortedPoints.get(0).y)
                || isPointInCircle(sortedPoints.get(3).x, sortedPoints.get(3).y, 200.0, sortedPoints.get(2).x, sortedPoints.get(2).y)){

            defaultTrap = true;
        }

        if (defaultTrap){
            sortedPoints.set(0, new Point(157.0, 342.0));
            sortedPoints.set(1, new Point(137.0, 1266.0));
            sortedPoints.set(2, new Point(843.0, 1255.0));
            sortedPoints.set(3, new Point(795.0, 329.0));
        }

        //p1 t0 p4 are in the anti clock wise order starting from top left
        // double s=source.get(0).x;

        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        Log.w("myApp", "bitmap width is "+width);
        Log.w("myApp", "bitmap height is "+height);

        Rect imageRect = new Rect(0, 0, width, height);



        /// To test the points order

        /*
        Point p1 = new Point(1.0*x,1.0*y );
        Point p2 = new Point(1.0*x+150.0,1.0*y+1.0*cropHeight);
        Point p3 = new Point(1.0*x+1.0*cropWidth,1.0*y+1.0*cropHeight);
        Point p4 = new Point(1.0*x+1.0*cropWidth,1.0*y);
        ArrayList<Point> source = new ArrayList<Point>();
        source.add(p1);
        source.add(p2);
        source.add(p3);
        source.add(p4);
        */


        Log.w("myApp", "from inside makedeafult inside cropimage calss, default crop rect values are set and now highlight view will be initiated ");

        HighlightView hv = new HighlightView(mImageView, imageRect, sortedPoints);

        Log.w("myApp", "higlight view initiated; done");

        mImageView.add(hv);
        Log.w("myApp", "add hv is done; done");

        mImageView.invalidate();
        mCrop = hv;

        Log.w("myApp", "mcrop=hv donee");
        mCrop.setFocus(false);;
    }



    boolean isInRectangle(double centerX, double centerY, double radius,
                          double x, double y)
    {
        return x >= centerX - radius && x <= centerX + radius &&
                y >= centerY - radius && y <= centerY + radius;
    }

    //test if coordinate (x, y) is within a radius from coordinate (center_x, center_y)
    public boolean isPointInCircle(double centerX, double centerY,
                                   double radius, double x, double y)
    {
        if(isInRectangle(centerX, centerY, radius, x, y))
        {
            double dx = centerX - x;
            double dy = centerY - y;
            dx *= dx;
            dy *= dy;
            double distanceSquared = dx + dy;
            double radiusSquared = radius * radius;
            return distanceSquared <= radiusSquared;
        }
        return false;
    }




    public int compare(Point o1, Point o2) {
        return Double.compare(o1.y, o2.y);
    }

    public static final int NO_STORAGE_ERROR  = -1;
    public static final int CANNOT_STAT_ERROR = -2;

    public static void showStorageToast(Activity activity) {

        showStorageToast(activity, calculatePicturesRemaining(activity));
    }

    public static void showStorageToast(Activity activity, int remaining) {

        String noStorageText = null;

        if (remaining == NO_STORAGE_ERROR) {

            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_CHECKING)) {

                noStorageText = "Prepping card";
            } else {

                noStorageText = "No Storage card";
            }
        } else if (remaining < 1) {

            noStorageText = "Not enough space";
        }

        if (noStorageText != null) {

            Toast.makeText(activity, noStorageText,Toast.LENGTH_SHORT).show();
        }
    }

    public static int calculatePicturesRemaining(Activity activity) {

        try {
            /*if (!ImageManager.hasStorage()) {
                return NO_STORAGE_ERROR;
            } else {*/
            String storageDirectory = "";
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                storageDirectory = Environment.getExternalStorageDirectory().toString();
            }
            else {
                storageDirectory = activity.getFilesDir().toString();
            }
            StatFs stat = new StatFs(storageDirectory);
            float remaining = ((float) stat.getAvailableBlocks()
                    * (float) stat.getBlockSize()) / 400000F;
            return (int) remaining;
            //}
        } catch (Exception ex) {
            // if we can't stat the filesystem then we don't know how many
            // pictures are remaining.  it might be zero but just leave it
            // blank since we really don't know.
            return CANNOT_STAT_ERROR;
        }
    }

}
