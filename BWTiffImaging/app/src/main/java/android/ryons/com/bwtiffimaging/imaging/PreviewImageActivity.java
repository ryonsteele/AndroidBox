package android.ryons.com.bwtiffimaging.imaging;


import android.ryons.com.bwtiffimaging.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageSubtractBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageThresholdEdgeDetection;

public class PreviewImageActivity extends Activity {

    private String sPath = null;
    ImageButton btnFilter1;
    ImageButton btnFilter2;
    ImageButton btnFilter3;
    ImageButton btnFilter4;
    Button btnSelect;
    ZoomableImageView touch;
    Bitmap imagePreview;
    Bitmap imageFiltered;
    Bitmap imageBlank;
    Bitmap Imgbutton1, Imgbutton2, Imgbutton3, Imgbutton4;
    // SeekBar seekBar;
    List<GPUImageFilter> filters = new ArrayList<>();
    int value=0;
    Float contLevelFloat = null;

    ProgressBar progress;

    boolean isSobelFilter = false;
    boolean isEdgeFilter = false;
    boolean isEdgeDifference = false;

    float mBlurFactor = 0.125f;
    float mContrast = 1.2f;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displayview);


        // Back to regularly scheduled inits

        btnFilter1 = (ImageButton)findViewById(R.id.button1);
        btnFilter2 = (ImageButton)findViewById(R.id.button2);
        btnFilter3 = (ImageButton)findViewById(R.id.button3);
        btnFilter4 = (ImageButton)findViewById(R.id.button4);
        btnSelect = (Button)findViewById(R.id.button5);

        touch = (ZoomableImageView)findViewById(R.id.imageView);

        progress = (ProgressBar) findViewById(R.id.progressBar);

        Intent i = this.getIntent();
        String path = i.getStringExtra("message");
        sPath=path;

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        progress.setVisibility(View.VISIBLE);
        try{   //decode tmp image in internal
            File f=new File(path, "tmp1.jpg");
            imagePreview = BitmapFactory.decodeStream(new FileInputStream(f));

        } catch (FileNotFoundException e) {
            Log.e("PreviewImageActivity", "LoadMainImageTask: Failed to load and decode preview image.", e);
        }


        new LoadMainImageTask().execute(sPath);

        value = 0;
        contLevelFloat = 4.0f;
        isSobelFilter = false;
        isEdgeFilter = false;
        isEdgeDifference = false;

        //set preview on click
        btnFilter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviewImageActivity.this.isSobelFilter = false;
                PreviewImageActivity.this.isEdgeFilter = false;
                PreviewImageActivity.this.isEdgeDifference = false;

                touch.setImageBitmap(getButton1FilteredImage());
            }
        });
        btnFilter1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("defaultfilter","0");
                editor.apply();

                PreviewImageActivity.this.isSobelFilter = false;
                PreviewImageActivity.this.isEdgeFilter = false;
                PreviewImageActivity.this.isEdgeDifference = false;

                touch.setImageBitmap(getButton1FilteredImage());
                Toast.makeText(PreviewImageActivity.this, "Default Saved!", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        btnFilter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviewImageActivity.this.isSobelFilter = false;
                PreviewImageActivity.this.isEdgeFilter = false;
                PreviewImageActivity.this.isEdgeDifference = false;

                touch.setImageBitmap(getButtonGrayscaleFilteredImage());
            }
        });

        btnFilter2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("defaultfilter","1");
                editor.apply();

                PreviewImageActivity.this.isSobelFilter = false;
                PreviewImageActivity.this.isEdgeFilter = false;
                PreviewImageActivity.this.isEdgeDifference = false;

                touch.setImageBitmap(getButtonGrayscaleFilteredImage());
                Toast.makeText(PreviewImageActivity.this, "Default Saved!", Toast.LENGTH_LONG).show();
                return true;
            }
        });


        btnFilter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviewImageActivity.this.isSobelFilter = true;
                PreviewImageActivity.this.isEdgeFilter = false;
                PreviewImageActivity.this.isEdgeDifference = false;

                touch.setImageBitmap(getButton2FilteredImage());
            }
        });

        btnFilter3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("defaultfilter","2");
                editor.apply();

                PreviewImageActivity.this.isSobelFilter = true;
                PreviewImageActivity.this.isEdgeFilter = false;
                PreviewImageActivity.this.isEdgeDifference = false;

                touch.setImageBitmap(getButton2FilteredImage());
                Toast.makeText(PreviewImageActivity.this, "Default Saved!", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        btnFilter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviewImageActivity.this.isSobelFilter = false;
                PreviewImageActivity.this.isEdgeFilter = false;
                PreviewImageActivity.this.isEdgeDifference = true;

                value = 3;  // Register that it's button 4

                new DoEdgeDifferenceTask().execute((Float)null);
            }
        });

        btnFilter4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("defaultfilter","3");
                editor.apply();

                PreviewImageActivity.this.isSobelFilter = false;
                PreviewImageActivity.this.isEdgeFilter = false;
                PreviewImageActivity.this.isEdgeDifference = true;

                value = 3;  // Register that it's button 4

                new DoEdgeDifferenceTask().execute((Float)null);
                Toast.makeText(PreviewImageActivity.this, "Default Saved!", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFilter();
            }
        });
    }

    @NonNull
    private GPUImage getEdgeDetectSubtractionImage(Float contrastVal) {

        GPUImage work = new GPUImage(getApplicationContext());
        GPUImage temp = new GPUImage(getApplicationContext());

        GPUImage edgeImage1 = new GPUImage(getApplicationContext());
        GPUImage edgeImage2 = new GPUImage(getApplicationContext());

        GPUImageSobelEdgeDetection edgeFilter1 = new GPUImageSobelEdgeDetection();
        GPUImageSobelEdgeDetection edgeFilter2 = new GPUImageSobelEdgeDetection();

        GPUImageSubtractBlendFilter blendFilter = new GPUImageSubtractBlendFilter();

        // Pick the line sizes to subtract for the edge detection (much trial and error)
        edgeFilter1.setLineSize(1f);      // Quarter size
        edgeFilter2.setLineSize(0.75f);   // Quarter size


        GPUImageFilterGroup beforeGroup = new GPUImageFilterGroup();


        beforeGroup.addFilter(new GPUImageBoxBlurFilter(mBlurFactor));
        beforeGroup.addFilter(new GPUImageContrastFilter(
                (contrastVal==null) ? mContrast : contrastVal));

        // Blur and contrast
        work.setImage(imagePreview);
        work.setFilter(beforeGroup);

        temp.setImage(work.getBitmapWithFilterApplied());

        // Perform two edge detections and subtract them
        edgeImage1.setImage(temp.getBitmapWithFilterApplied());
        edgeImage1.setFilter(edgeFilter1);
        blendFilter.setBitmap(edgeImage1.getBitmapWithFilterApplied());

        edgeImage2.setImage(temp.getBitmapWithFilterApplied());
        edgeImage2.setFilter(edgeFilter2);
        blendFilter.setBitmap(edgeImage2.getBitmapWithFilterApplied());

        work.setImage(blendFilter.getBitmap()); // get bitmap from blend filter

        // Invert and apply levels
        GPUImageFilterGroup afterGroup = new GPUImageFilterGroup();
        afterGroup.addFilter(new GPUImageColorInvertFilter());

        GPUImageLevelsFilter levelsFilter = new GPUImageLevelsFilter();
        levelsFilter.setMin(0.5f, 0.75f, 1.0f);
        afterGroup.addFilter(levelsFilter);

        work.setFilter(afterGroup);

        return work;
    }


    private class LoadMainImageTask extends AsyncTask<String, Void, Void> {
        Bitmap touchImg;

        @Override
        protected void onPreExecute() {

            touchImg = getButton1FilteredImage();
            imageBlank = Bitmap.createScaledBitmap(touchImg, 250, 350, false);
            imageBlank.eraseColor(Color.BLACK);

            // Set all buttons to imageBlank to get initial sizing right
            btnFilter1.setImageBitmap(imageBlank);
            btnFilter2.setImageBitmap(imageBlank);
            btnFilter3.setImageBitmap(imageBlank);
            btnFilter4.setImageBitmap(imageBlank);

            touch.setImageBitmap(touchImg);
        }

        @Override
        protected Void doInBackground(String... params) {
            String path = params[0];

            imageFiltered = imagePreview.copy(imagePreview.getConfig(), imagePreview.isMutable());

//               // int height = imagePreview.getHeight()/3;
//               // int width = imagePreview.getWidth()/3;
//
//                 //Quarter the size of the bitmap for speed; makes YUGE difference //ryons removed due to not being able to get reasonable quality
////                imagePreview = Bitmap.createScaledBitmap(
////                        imagePreview, width*2, height*2, true);

            Imgbutton1 =Bitmap.createScaledBitmap(getButton1FilteredImage(), 250, 350, true);
            Imgbutton2= Bitmap.createScaledBitmap(getButtonGrayscaleFilteredImage(), 250, 350, true);
            Imgbutton3 = Bitmap.createScaledBitmap(getButton2FilteredImage(), 250, 350, true);
            Imgbutton4 = Bitmap.createScaledBitmap(
                    // This is a quick-rendering placeholder,
                    // and does NOT reflect the true filter behavior
                    applyFilter(
                            new GPUImageThresholdEdgeDetection(),
                            new GPUImageContrastFilter(0.75f),
                            null ,2, true),
                    250, 350, true);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            btnFilter1.setImageBitmap(Imgbutton1);
            btnFilter2.setImageBitmap(Imgbutton2);
            btnFilter3.setImageBitmap(Imgbutton3);
            btnFilter4.setImageBitmap(Imgbutton4);

            progress.setVisibility(View.GONE);  // Might as well put this here as anywhere
            value=0; //after last button is loaded reset callback value to zero (first thumnail option)
        }
    }

    private class DoEdgeDifferenceTask extends AsyncTask<Float, Void, Void> {
        GPUImage work = null;

        @Override
        protected void onPreExecute() {
            touch.setFocusableInTouchMode(false);
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Float... params) {
            if (params.length == 0) {
                Log.e("PreviewImageActivity", "No contrast value passed to DoEdgeDifferenceTask.exec().");
                return null;
            }
            Float contrastVal = params[0];
            work = getEdgeDetectSubtractionImage(contrastVal);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (work != null) {
                touch.setImageBitmap(work.getBitmapWithFilterApplied());
//                touch.setImage(work.getBitmapWithFilterApplied());
            }
            progress.setVisibility(View.GONE);
            touch.setFocusableInTouchMode(true);
        }
    }

    private Bitmap getButton1FilteredImage() {
        value = 0;
        Mat imageMat = new Mat();
        Bitmap src = imagePreview.copy(imagePreview.getConfig(), imagePreview.isMutable());
        Utils.bitmapToMat(src, imageMat);
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(imageMat, imageMat, new Size(5, 5), 0);
        Imgproc.adaptiveThreshold(imageMat, imageMat, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 19, 6);
        Utils.matToBitmap(imageMat, src);

        return src;
    }

    private Bitmap getButtonGrayscaleFilteredImage() {
        return applyFilter(
                new GPUImageGrayscaleFilter(),
                new GPUImageContrastFilter(1.5f),
                new GPUImageBilateralFilter(), 1, true);
    }


    private Bitmap getButton2FilteredImage() {
        return applyFilter(
                new GPUImageThresholdEdgeDetection()/*GPUImageSobelThresholdFilter()*/,
                new GPUImageContrastFilter(2.0f),
                null ,2, true);
    }

    private Bitmap applyFilter(GPUImageFilter one, GPUImageFilter two, GPUImageFilter three, int val, boolean clear){

        GPUImage mGPUImage = new GPUImage(getApplicationContext());

        filters = configFilters(one, two, three, val, clear);
        System.gc();

        mGPUImage.setFilter(new GPUImageFilterGroup(filters));
        imageFiltered = mGPUImage.getBitmapWithFilterApplied(imagePreview);

        return imageFiltered;
    }

    private synchronized List<GPUImageFilter> configFilters(
            GPUImageFilter one, GPUImageFilter two, GPUImageFilter three, int val, boolean clear) {

        Log.d("PreviewImageActivity", "configFilters before: "+ filters);

        if(clear) {
            filters = new ArrayList<>();
            value = val;

            if(one != null)
                filters.add(one);
            if(two != null)
                filters.add(two);
            if(three != null)
                filters.add(three);
        } else {
            value = val;
            if (isSobelFilter) {
                filters.set(0, one);
            } else if (isEdgeFilter) {
                filters.set(0, one);
            } else {
                filters.set(1, two);
            }
        }

        Log.d("PreviewImageActivity", "configFilters after: "+ filters);
        return filters;
    }

    private void selectFilter(){
        Log.d("PreviewImageActivity",
                "Returning from Preview: " +
                        "value=["+ value +"], " +
                        "contLevelFloat=["+ contLevelFloat +"].");

        Intent data = new Intent();
        data.putExtra("filters", value);
        data.putExtra("contrast", contLevelFloat);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onStop(){
        //cleanup
        File file = new File(sPath,"tmp1.jpg");
        boolean deleted = file.delete();

        if(!deleted){
            Log.d("PreviewImageActivity", "failed to delete temp image" + sPath);
        }
        super.onStop();

    }
}

