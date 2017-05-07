package android.ryons.com.bwtiffimaging.imaging;


import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.ryons.com.bwtiffimaging.ImageUtil;
import android.util.Log;

import java.io.File;

public class ThumbnailViewItem {

    private final Bitmap mThumbnail; // the drawable for the ListView item ImageView
    private final String mPath;

    public ThumbnailViewItem(Bitmap thumbnail, String path) {
        this.mThumbnail = thumbnail;
        this.mPath = path;
    }

    public String getType() {
        if (!mPath.contains(".")) {
            return "txt";
        } else {
            return "image/" + mPath.substring(mPath.lastIndexOf(".") + 1);
        }
    }

//    public Bitmap getBitmap() {
//        String resolvedPath =
//                ImageUtil.getFilePath(,
//                        Uri.parse("file://" + mPath));
//        Log.e(Util.getTag(), "resolvedPath="+ resolvedPath);
//
//        File actualFile = new File(resolvedPath);
//
//        if (actualFile.exists()) {
//            String absolutePath = actualFile.getAbsolutePath();
//            return BitmapFactory.decodeFile(absolutePath);
//        }
//        return null;
//    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public String getPath() {
        return mPath;
    }
}
