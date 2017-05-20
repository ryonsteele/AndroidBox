package android.ryons.com.bwtiffimaging.imaging;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.ryons.com.bwtiffimaging.ContextWrapperInt;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.ryons.com.bwtiffimaging.Util;
import android.ryons.com.bwtiffimaging.R;

import org.beyka.tiffbitmapfactory.CompressionScheme;
import org.beyka.tiffbitmapfactory.TiffSaver;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
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


public class CreateImageActivity extends AppCompatActivity {
    static{ System.loadLibrary("opencv_java3"); }

    public static final int CAPTURE_IMAGE = 0;
    public static final int SELECT_IMAGE = 1;
    public static final int CROP = 2;
    public static final int PREVIEW_REQUEST = 3;

    Handler uiHandler = new Handler();
    ProgressBar pb;
    ProgressDialog pd;


    Dialog dialog;

    Button btnImage;
    Bitmap image;
    Bitmap imageFiltered;

    Uri imageUri;
    GridView gridView;
    ThumbnailGridViewAdapter gridViewAdapter;

    String parentRowId;
    String parentRowType;
    String absPath;
    File sendDirectory;
    Boolean firstPass = false;
    boolean isFax4 =false;
    boolean isPreview = false;
    SharedPreferences preferences;

    int temPos = 0;
    String previewPath = "";
    List<GPUImageFilter> filters = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_create);

        ContextWrapperInt.CONTEXT = getApplicationContext();

        pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //lowRes = ImageUtils.getBackCameraResolutionInMp();
        sendDirectory = new File(Environment.getExternalStorageDirectory() + "/tmp");
        if(sendDirectory != null && sendDirectory.exists() && sendDirectory.listFiles() != null ){
            for (File toDel: sendDirectory.listFiles()) {
                toDel.delete();
            }
        }

        Intent i = this.getIntent();
        parentRowId = i.getStringExtra("id");
        parentRowType = i.getStringExtra("type");


        btnImage = (Button)findViewById(R.id.btnImage);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        gridView = (GridView)findViewById(R.id.gridView);
        gridViewAdapter = new ThumbnailGridViewAdapter(this);
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateImageActivity.this);
                builder.setMessage("Delete image?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gridViewAdapter.removeItem(position);

                            ContextWrapper cw = new ContextWrapper(getApplicationContext());
                            // path to /app_data/imageDir
                            File directory = sendDirectory;
                            // Create imageDir
                            File mypath = new File(directory, position+".jpg");
                            mypath.delete();
                            temPos--;
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                temPos = position;
                Log.d("BWTiffImaging", "Previewing image at position ["+ temPos +"]...");

                ThumbnailViewItem item =
                        (ThumbnailViewItem) ((GridView) parent).getAdapter().getItem(position);
                if (item != null) {
                    previewPath = item.getPath();
                    Log.d("BWTiffImaging", "Path for image ["+ temPos +"]: "+ previewPath);
                }

                previewImage();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.report_send, menu);
        return true;
    }

    // Called when menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_report_send:
                sendImage();
                break;
        }
        return true;
    }


    private void selectImage(){
        dialog = new Dialog(CreateImageActivity.this);
        dialog.setContentView(R.layout.attachment_dialog);
        dialog.setTitle("Select Image");
        dialog.setCancelable(true);
        Button btnCaptureImage = (Button)dialog.findViewById(R.id.btnCaptureImage);
        Button btnSelectImage = (Button)dialog.findViewById(R.id.btnSelectImage);
        dialog.findViewById(R.id.btnSelectFile).setVisibility(View.GONE);

        // Verify that camera is available. If not, disable the camera button
        PackageManager pm = this.getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            btnCaptureImage.setVisibility(View.GONE);
        } else {
            btnCaptureImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(Util.isIntentAvailable(CreateImageActivity.this,
                            MediaStore.ACTION_IMAGE_CAPTURE)){
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        imageUri = getOutputMediaFileUri();
                        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(i, CAPTURE_IMAGE);
                    }
                }
            });
        }

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_IMAGE);
            }
        });
        dialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAPTURE_IMAGE:
                    dialog.dismiss();
                    // rescan the image so it shows in the gallery
                    getApplicationContext().sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            imageUri
                    ));
                    performCrop(imageUri);
                    break;
                case SELECT_IMAGE:
                    dialog.dismiss();
                    if(data!=null) {
                        try {
                            String imagePath = ImageUtils.getImagePath(data.getData());
                            if (!imagePath.startsWith("file://")) {
                                imagePath = "file://"+ imagePath;
                            }
                            imageUri = Uri.parse(imagePath);
                            addFirstTryImageFilters();
                            new ProcessImageTask().execute(imageUri.getPath());
                        } catch (Exception ex) {
                            Toast.makeText(CreateImageActivity.this,
                                    "Could not get image from gallery. " + ex.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case CROP:
                    if (!ImageUtils.blurCheck(imageUri)) {
                        addFirstTryImageFilters();
                        String path = data.getStringExtra(CropImage.IMAGE_PATH);

                        new ProcessImageTask().execute(path);
                        firstPass = false;
                    } else {
                        if(firstPass) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);

                            builder.setTitle("Blur Check");
                            builder.setMessage("Detecting blur - Use anyway?");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    firstPass = false;
                                    dialog.dismiss();
                                    addFirstTryImageFilters();
                                    new ProcessImageTask().execute(imageUri.getPath());
                                }
                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firstPass = true;
                                    dialog.dismiss();
                                    Toast toast = Toast.makeText(CreateImageActivity.this,
                                            "Image too blurred for processing, please try again. ",
                                            Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    imageUri = null;
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else{
                            firstPass = true;
                            Toast toast = Toast.makeText(CreateImageActivity.this,
                                    "Image too blurred for processing, please try again. ",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            imageUri = null;
                        }
                    }
                    break;
                case PREVIEW_REQUEST:
                    int val = 0;
                    if (data!=null) {
                        Bundle extras = data.getExtras();

                        if (extras != null) {
                            val = extras.getInt("filters");
                        }
                        if (val == 0) {
                            filters.clear();
                            addFirstTryImageFilters();
                        }
                        else if (val == 1) {
                            filters.clear();
                            filters.add(new GPUImageGrayscaleFilter());
                            filters.add(new GPUImageContrastFilter(2.0f));
                            filters.add(new GPUImageBilateralFilter());
                        }
                        else if(val == 2) {
                            filters.clear();
                            filters.add(new GPUImageThresholdEdgeDetection());
                            filters.add(new GPUImageContrastFilter(1.5f));
                        }
                        else if(val == 3) {
                            filters.clear();
                        }

                        File directory = sendDirectory;
                        File mypath = new File(directory, temPos +".png");
                        mypath.delete();
                        new ProcessPreviewTask(val).execute(previewPath);
                    }
                    break;
            }
        }
    }

    private void addFirstTryImageFilters() {
        filters.clear();
        filters.add(new GPUImageGrayscaleFilter());
        filters.add(new GPUImageContrastFilter(1.5f));
    }

    class ProcessImageTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(CreateImageActivity.this, "", "Processing image...", true);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String path = params[0];
                previewPath = path;
                String imagePath = path;
                if (!imagePath.startsWith("file://")) {
                    imagePath = "file://"+ imagePath;
                }
                imageUri = Uri.parse(imagePath);
                // Load the original image from [path]
                FileInputStream fis = new FileInputStream(path);
                image = BitmapFactory.decodeStream(fis);
                image = Bitmap.createScaledBitmap(image, 1728, 1728 * image.getHeight()/image.getWidth(), false);

                isPreview = preferences.getBoolean("preview", false);

                if(!isPreview)
                    image= applyFilterDefault(image);


                if(!sendDirectory.exists())
                    sendDirectory.mkdir();


                // Create 0.jpg, 1.jpg, etc. in imageSendDir
                File myPath = new File(sendDirectory, temPos + ".jpg");
                if(myPath.exists()){

                    temPos++;
                    myPath = new File(sendDirectory, temPos + ".jpg");
                }

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(myPath);
                    // Use the compress method on the BitMap object to write image to OutputStream
                    int quality = 100;

                    image.compress(Bitmap.CompressFormat.JPEG, quality, fos);
                    Log.d("BWTiffImaging", "Image saved to "+ myPath.getAbsolutePath());
                } catch (Exception e) {
                    Log.e("BWTiffImaging",
                            "Unable to compress/save image " +
                                    "["+ myPath.getAbsolutePath() +"].", e);
                } finally {
                    try {
                        fos.close();
                    } catch (Exception e) {
                        Log.e("BWTiffImaging",
                                "Unable to close file for image " +
                                        "["+ myPath.getAbsolutePath() +"].", e);
                    }
                }
                Log.d("BWTiffImaging", "Saved and closed image to "+ myPath.getAbsolutePath());
                Log.d("BWTiffImaging", "JPG Image file size: "+ myPath.length());

            } catch (Exception ex) {
                Log.e(Util.getTag(getApplicationContext()),
                        "Unable to compress/save image.", ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();

            if (null != image ){//&& null != imageFiltered) {
                    setThumbnail();
                    new PreviewImageTask().execute();
            } else {
                Toast.makeText(CreateImageActivity.this,
                        "Could not load image directly from camera. " +
                                "You may need to manually select the image from the gallery.",
                        Toast.LENGTH_LONG).show();
            }
            pd.dismiss();

            if(isPreview && null != image)
                new PreviewImageTask().execute();
        }
    }


    class ProcessPreviewTask extends AsyncTask<String, Void, Void> {
        int val = 0;

        public ProcessPreviewTask(int val) {
            this.val = val;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(CreateImageActivity.this, "", "Processing image...", true);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String path = params[0];
                String imagePath = path;
                if (!imagePath.startsWith("file://")) {
                    imagePath = "file://"+ imagePath;
                }

                imageUri = Uri.parse(imagePath);
//                imageFiltered = applyFilter(image);
                GPUImage mGPUImage = new GPUImage(getApplicationContext());

                // Apply filters set before calling the Task
                mGPUImage.setFilter(new GPUImageFilterGroup(filters));

                // Load the original image from [path]
                FileInputStream fis = new FileInputStream(path);
                image = BitmapFactory.decodeStream(fis);
                image = Bitmap.createScaledBitmap(image, 1728, 1728 * image.getHeight()/image.getWidth(), false);
                if(val == 0){
                    imageFiltered = mGPUImage.getBitmapWithFilterApplied(image.copy(image.getConfig(), image.isMutable()));
                    imageFiltered = applyFilter(imageFiltered);
                }
                else if ( val != 3) {
                    imageFiltered = mGPUImage.getBitmapWithFilterApplied(image.copy(image.getConfig(), image.isMutable()));
                } else {
                    // Val == 3 && !isInitialPreview
                    // Build [imageFiltered] per the big edge filter; apply the contrast too!
                    imageFiltered = getEdgeDetectSubtractionImage(image.copy(image.getConfig(), image.isMutable()), 1.5f)
                            .getBitmapWithFilterApplied();
                }

                if(!sendDirectory.exists())
                    sendDirectory.mkdir();


                // Create 0.jpg, 1.jpg, etc. in imageSendDir
                File myPath = new File(sendDirectory, temPos + ".jpg");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(myPath);
                    // Use the compress method on the BitMap object to write image to OutputStream
                    int quality = 100;


                    imageFiltered.compress(Bitmap.CompressFormat.JPEG, quality, fos);
                    Log.d("BWTiffImaging", "Image saved to "+ myPath.getAbsolutePath());
                } catch (Exception e) {
                    Log.e("BWTiffImaging",
                            "Unable to compress/save image " +
                                    "["+ myPath.getAbsolutePath() +"].", e);
                } finally {
                    try {
                        fos.close();
                    } catch (Exception e) {
                        Log.e("BWTiffImaging",
                                "Unable to close file for image " +
                                        "["+ myPath.getAbsolutePath() +"].", e);
                    }
                }
                Log.d("BWTiffImaging", "Saved and closed image to "+ myPath.getAbsolutePath());
                Log.d("BWTiffImaging", "JPG Image file size: "+ myPath.length());


            } catch (Exception ex) {
                Log.e(Util.getTag(getApplicationContext()),
                        "Unable to compress/save image.", ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();
        }
    }


    public void setThumbnail() {

        GPUImage mGPUImage = new GPUImage(getApplicationContext());
        // Apply filters set before calling the Task
        mGPUImage.setFilter(new GPUImageFilterGroup(filters));
        imageFiltered = mGPUImage.getBitmapWithFilterApplied(image.copy(image.getConfig(), image.isMutable()));
        imageFiltered = applyFilter(imageFiltered);
        Bitmap bmpThumb = Bitmap.createScaledBitmap(imageFiltered, 350, 450, true);
        gridViewAdapter.addItem(new ThumbnailViewItem(bmpThumb, imageUri.getPath()));
    }

    private void performCrop(Uri picUri) {
        try {

            Intent intent = new Intent(this,CropImage.class);
            intent.putExtra(CropImage.IMAGE_PATH, picUri.getPath());
            intent.putExtra(CropImage.SCALE, true);

            intent.putExtra(CropImage.ASPECT_X, 0);
            intent.putExtra(CropImage.ASPECT_Y, 0);
            startActivityForResult(intent, CROP);
        }
        catch (ActivityNotFoundException anfe) {
            // ignored. if it can't be cropped, so be it.
            Log.e(Util.getTag(getApplicationContext()), "Unable start Crop.", anfe);
        }
    }

    private byte[] encodeImage() {

        FileInputStream fis=null;
        File img = null;

        try {
            File folder = new File(Environment.getExternalStorageDirectory() + "/tmp/send");
            if (!folder.exists()) {
                folder.mkdir();
            }

            BitmapFactory.Options opt = new BitmapFactory.Options();
            Bitmap bitmap =  BitmapFactory.decodeFile(sendDirectory.listFiles()[0].getAbsolutePath(), opt);
            TiffSaver.SaveOptions options = new TiffSaver.SaveOptions();

            isFax4 = preferences.getBoolean("compression", false);

            if(!isFax4)
                options.compressionScheme = CompressionScheme.COMPRESSION_LZW;
            else
                options.compressionScheme = CompressionScheme.COMPRESSION_CCITTFAX4;

            options.author = "SomeCo";
            boolean saved = TiffSaver.saveBitmap(folder + "/outFinal.tiff", bitmap, options);
            img = new File(folder + "/outFinal.tiff");
            sendDirectory.listFiles()[0].delete();

            File[] children = sendDirectory.listFiles();

            for (int k=0; k < children.length ; k++) {

                if(!children[k].isDirectory()) {
                    Bitmap bitmap1 = BitmapFactory.decodeFile(children[k].getAbsolutePath());
                    boolean saved1 = TiffSaver.appendBitmap(img, bitmap1, options);
                }
            }

            if(fis != null)fis.close();
            fis =  new FileInputStream(img.getAbsolutePath());
            byte[] buffers =   new byte[(int) fis.getChannel().size()];
            fis.read(buffers);

            return buffers;

        } catch (Exception e) {
            Log.e(Util.getTag(getApplicationContext()), "Unable to encode TIFF.", e);
        }finally {
            try{
                if(fis != null)fis.close();
                if(img != null)img.delete();
            }catch(Exception ex){
                Log.e(Util.getTag(getApplicationContext()), "Failed to dispose resources.", ex);
            }
        }

        return null;
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(ImageUtils.getOutputMediaFile());
    }


    private void previewImage() {
        new PreviewImageTask().execute();
    }

    class PreviewImageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(CreateImageActivity.this, "", "Saving Image...", true);
        }

        @Override
        protected Void doInBackground(String... params) {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());

            // Get path for temPos image, set from grid adapter
            File originalPath = new File(previewPath);

            // path to /app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

            // Create imageDir
            File tempPath = new File(directory, "tmp1.jpg");
            ImageUtils.makeTempImageCopy(originalPath, tempPath);  // Copy original image to tmp1.jpg
            absPath = directory.getAbsolutePath();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();

            if (null == absPath) {
                Toast.makeText(CreateImageActivity.this,
                        "Failed to save.",
                        Toast.LENGTH_LONG).show();
            }

            Intent searchIntent = new Intent();
            searchIntent.setClass(getApplicationContext(), PreviewImageActivity.class);
            searchIntent.putExtra("message", absPath);
            startActivityForResult(searchIntent, PREVIEW_REQUEST);

        }
    }

    private void sendImage() {

        if(gridViewAdapter.getCount() < 1) {
            Toast.makeText(CreateImageActivity.this,
                    "An image is required to create this record.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        pd = ProgressDialog.show(CreateImageActivity.this, "", "Encoding and sending image...");
        Thread t = new Thread() {
            @Override public void run() {

                try {

                    byte[] encodedImage = encodeImage();//stream.toByteArray() multipage tiff;

                    Log.e(Util.getTag(getApplicationContext()),
                            "Byte Array Size: " + encodedImage.length);

                    imageFiltered = null;
                    System.gc();

                } catch(Exception ex) {
                    Log.e(Util.getTag(getApplicationContext()),
                            "Unable to create new image. " + ex.getMessage());
                } finally {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                        }
                    });
                    for (int i =0; i < sendDirectory.list().length; i++) {
                        sendDirectory.listFiles()[i].delete();
                    }

                }
            }
        };
        t.start();

        //cleanup gridview to prevent user error
        for(int i = 0; i < gridViewAdapter.getCount(); i++) {
            gridViewAdapter.removeItem(i);
        }
    }

    /****
     * getEdgeDetectSubtractionImage() here is taken from {@link PreviewImageActivity}.
     * Always defer to that version for accuracy.
     */
    float mBlurFactor = 0.125f;
    float mContrast = 1.2f;

    private GPUImage getEdgeDetectSubtractionImage(Bitmap originalImage, Float contrastVal) {

        GPUImage work = new GPUImage(getApplicationContext());
        GPUImage temp = new GPUImage(getApplicationContext());

        GPUImage edgeImage1 = new GPUImage(getApplicationContext());
        GPUImage edgeImage2 = new GPUImage(getApplicationContext());

        GPUImageSobelEdgeDetection edgeFilter1 = new GPUImageSobelEdgeDetection();
        GPUImageSobelEdgeDetection edgeFilter2 = new GPUImageSobelEdgeDetection();

        GPUImageSubtractBlendFilter blendFilter = new GPUImageSubtractBlendFilter();

        // Pick the line sizes to subtract for the edge detection (much trial and error)
        edgeFilter1.setLineSize(2f);    //**
        edgeFilter2.setLineSize(1.5f);

        GPUImageFilterGroup beforeGroup = new GPUImageFilterGroup();
        beforeGroup.addFilter(new GPUImageGaussianBlurFilter(mBlurFactor));
        beforeGroup.addFilter(new GPUImageContrastFilter(
                (contrastVal==null) ? mContrast : contrastVal));

        // Blur and contrast
        work.setImage(originalImage);
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

    private Bitmap applyFilterDefault(Bitmap input){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sVal = preferences.getString("defaultfilter", null);
        int val = 0;
        filters.clear();

        if(sVal != null) val = Integer.parseInt(sVal);

        if (val == 0) {
            addFirstTryImageFilters();
        }
        else if (val == 1) {
            filters.add(new GPUImageGrayscaleFilter());
            filters.add(new GPUImageContrastFilter(2.0f));
            filters.add(new GPUImageBilateralFilter());
        }
        else if(val == 2) {
            filters.add(new GPUImageThresholdEdgeDetection());
            filters.add(new GPUImageContrastFilter(1.5f));
        }


        GPUImage mGPUImage = new GPUImage(getApplicationContext());

        // Apply filters set before calling the Task
        mGPUImage.setFilter(new GPUImageFilterGroup(filters));

        if(val == 0){
            image = mGPUImage.getBitmapWithFilterApplied(image);
            imageFiltered = applyFilter(image.copy(image.getConfig(), image.isMutable()));
        }
        else if ( val != 3) {
            imageFiltered = mGPUImage.getBitmapWithFilterApplied(image.copy(image.getConfig(), image.isMutable()));
        } else {
            // Val == 3 && !isInitialPreview
            // Build [imageFiltered] per the big edge filter; apply the contrast too!
            imageFiltered = getEdgeDetectSubtractionImage(image.copy(image.getConfig(), image.isMutable()), 1.5f)
                    .getBitmapWithFilterApplied();
        }

        return imageFiltered;
    }


    private Bitmap applyFilter(Bitmap input){

        Mat imageMat = new Mat();
        Bitmap src = input.copy(input.getConfig(), input.isMutable());
        Utils.bitmapToMat(src, imageMat);
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(imageMat, imageMat, new Size(5, 5), 0);
        Imgproc.adaptiveThreshold(imageMat, imageMat, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 19, 6);
        Utils.matToBitmap(imageMat, src);

        return src;
    }

    @Override
    protected void onDestroy() {
        for (int i =0; i < sendDirectory.list().length; i++) {
            sendDirectory.listFiles()[i].delete();
        }
        super.onDestroy();
    }
}