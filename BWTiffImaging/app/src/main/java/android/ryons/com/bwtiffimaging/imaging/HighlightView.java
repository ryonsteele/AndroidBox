package android.ryons.com.bwtiffimaging.imaging;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import android.ryons.com.bwtiffimaging.R;

import org.opencv.core.Point;

import java.util.ArrayList;


// This class is used by CropImage to display a highlighted cropping trapezoid
// overlayed with the image. There are two coordinate spaces in use. One is
// image, another is screen. computeLayout() uses mMatrix to map from image
// space to screen space.
class HighlightView {

    Matrix mMatrix;

    enum ModifyMode {
        None, Move, Grow
    }

    @SuppressWarnings("unused")
    private static final String LOG_TAG = HighlightView.class.getSimpleName();
    private View mContext; // The View displaying the image.

    /* used during onDraw */
    private final Rect mViewDrawingRect = new Rect();
    private final Rect mLeftRect = new Rect();
    private final Rect mRightRect = new Rect();
    private final Rect mTopRect = new Rect();
    private final Rect mBottomRect = new Rect();

    private ModifyMode mMode = ModifyMode.None;
    private final CroppingTrapezoid mTrapzoid;
    boolean mIsFocused;
    boolean mHidden = false;

    Rect mDrawRect; // in screen space

    private Drawable mResizeDrawableWidth;
    private Drawable mResizeDrawableHeight;

    private final Paint mFocusPaint = new Paint();
    private final Paint mOutlinePaint = new Paint();
    private final int mCropCornerHandleRadius;
    private final int mCropEdgeHandleRadius;
    private final float mHysteresis;


    public static final int GROW_NONE = 0;
    public static final int GROW_LEFT_EDGE = (1 << 1);
    public static final int GROW_RIGHT_EDGE = (1 << 2);
    public static final int GROW_TOP_EDGE = (1 << 3);
    public static final int GROW_BOTTOM_EDGE = (1 << 4);
    public static final int MOVE = (1 << 5);


    public HighlightView(ImageView ctx, Rect imageRect,ArrayList<Point> cropRect) {
        Log.w("myApp", "from inside highlight view ");

        mContext = ctx;
        final int progressColor = mContext.getResources().getColor(R.color.progress_color);
        mCropCornerHandleRadius = mContext.getResources().getDimensionPixelSize(R.dimen.crop_handle_corner_radius);
        mCropEdgeHandleRadius = mContext.getResources().getDimensionPixelSize(R.dimen.crop_handle_edge_radius);
        mHysteresis = mContext.getResources().getDimensionPixelSize(R.dimen.crop_hit_hysteresis);
        final int edgeWidth = mContext.getResources().getDimensionPixelSize(R.dimen.crop_edge_width);

        mMatrix = new Matrix(ctx.getImageMatrix());
        // Log.i(LOG_TAG, "image = " + imageRect.toString() + " crop = " + cropRect.toString());
        mTrapzoid = new CroppingTrapezoid(cropRect, imageRect);

        mDrawRect = computeLayout();

        Log.w("myApp", "inside hv contructor; computelayout() is called and done ");

        mFocusPaint.setARGB(125, 50, 50, 50);
        mFocusPaint.setStyle(Paint.Style.FILL);

        mOutlinePaint.setARGB(0xFF, Color.red(progressColor), Color.green(progressColor), Color.blue(progressColor));
        mOutlinePaint.setStrokeWidth(edgeWidth);
        mOutlinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mOutlinePaint.setAntiAlias(true);

        mMode = ModifyMode.None;
        android.content.res.Resources resources = mContext.getResources();
        mResizeDrawableWidth = resources.getDrawable(R.drawable.camera_crop_width);
        mResizeDrawableHeight = resources.getDrawable(R.drawable.camera_crop_height);
    }
    public boolean hasFocus() {

        return mIsFocused;
    }


    public void setFocus(boolean f) {
        mIsFocused = f;
    }

    protected void draw(Canvas canvas) {
        if (mHidden) {
            return;
        }
        mDrawRect = computeLayout();
        drawEdges(canvas);

    }

    private void drawEdges(Canvas canvas) {

        Log.w("myApp", " drawing the edges ");

        mContext.getDrawingRect(mViewDrawingRect);
        mTopRect.set(0, 0, mViewDrawingRect.right, mDrawRect.top);
        mRightRect.set(0, mDrawRect.top, mDrawRect.left, mDrawRect.bottom);
        mLeftRect.set(mDrawRect.right, mDrawRect.top, mViewDrawingRect.right, mDrawRect.bottom);
        mBottomRect.set(0, mDrawRect.bottom, mViewDrawingRect.right, mViewDrawingRect.bottom);

        /* debug
        Paint paint = new Paint(mOutlinePaint);
        paint.setColor(0xffff0000);
        final Rect perspectiveCorrectedBoundingRect = mTrapzoid.getPerspectiveCorrectedBoundingRect();
        RectF debug = new RectF();
        RectF src = new RectF(perspectiveCorrectedBoundingRect);
        mMatrix.mapRect(debug,src);
        canvas.drawRect(debug,paint);
        */


        canvas.drawRect(mTopRect, mFocusPaint);
        canvas.drawRect(mRightRect, mFocusPaint);
        canvas.drawRect(mLeftRect, mFocusPaint);
        canvas.drawRect(mBottomRect, mFocusPaint);
        canvas.save();
        canvas.clipRect(mDrawRect);

        //p are screen points

        //p0 p1 are x,y of top left
        //p2 p3 are x,y of top right
        //p4 p5 are x,y of bottom right
        //p6 p7 are x,y of bottom left

        //path is acontour and the inside the contour will be shown as a focused
        final float[] p = mTrapzoid.getScreenPoints(mMatrix);
        Path path = new Path();
        path.moveTo((int) p[0], (int) p[1]);
        path.lineTo((int) p[2], (int) p[3]);
        path.lineTo((int) p[4], (int) p[5]);
        path.lineTo((int) p[6], (int) p[7]);
        path.close();
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        canvas.drawPath(path, mFocusPaint);
        canvas.restore();


        Log.w("myApp", "screen points are");
        Log.w("myApp", "p0 " + p[0]);
        Log.w("myApp", "p1 "+p[1]);
        Log.w("myApp", "p2 "+p[2]);
        Log.w("myApp", "p3 "+p[3]);
        Log.w("myApp", "p4 "+p[4]);
        Log.w("myApp", "p5 "+p[5]);
        Log.w("myApp", "p6 "+p[6]);
        Log.w("myApp", "p7 "+p[7]);

        //line draw explicit lines throug the path


        Log.w("myApp", " now draing the lines ");


        canvas.drawLine(p[0], p[1], p[2], p[3], mOutlinePaint);
        canvas.drawLine(p[2], p[3], p[4], p[5], mOutlinePaint);
        canvas.drawLine(p[4], p[5], p[6], p[7], mOutlinePaint);
        canvas.drawLine(p[0], p[1], p[6], p[7], mOutlinePaint);

        canvas.drawCircle(p[0], p[1], mCropCornerHandleRadius, mOutlinePaint);
        canvas.drawCircle(p[2], p[3], mCropCornerHandleRadius, mOutlinePaint);
        canvas.drawCircle(p[4], p[5], mCropCornerHandleRadius, mOutlinePaint);
        canvas.drawCircle(p[6], p[7], mCropCornerHandleRadius, mOutlinePaint);

        float x = (p[0]+p[2])/2;
        float y = (p[1]+p[3])/2;
        canvas.drawCircle(x, y, mCropEdgeHandleRadius, mOutlinePaint);
        x = (p[2]+p[4])/2;
        y = (p[3]+p[5])/2;
        canvas.drawCircle(x, y, mCropEdgeHandleRadius, mOutlinePaint);
        x = (p[4]+p[6])/2;
        y = (p[5]+p[7])/2;
        canvas.drawCircle(x, y, mCropEdgeHandleRadius, mOutlinePaint);
        x = (p[0]+p[6])/2;
        y = (p[1]+p[7])/2;
        canvas.drawCircle(x, y, mCropEdgeHandleRadius, mOutlinePaint);


    }

    public void setMode(ModifyMode mode) {
        if (mode != mMode) {
            mMode = mode;
            mContext.invalidate();
        }
    }


    // Determines which edges are hit by touching at (x, y).
    public int getHit(float x, float y, float scale) {
        // convert hysteresis to imagespace
        final float hysteresis = mHysteresis / scale;
        return mTrapzoid.getHit(x, y, hysteresis);
    }


    // Handles motion (dx, dy) in screen space.
    // The "edge" parameter specifies which edges the user is dragging.
    void handleMotion(int edge, float dx, float dy) {
        if (edge == GROW_NONE) {
            return;
        } else if (edge == MOVE) {
            mTrapzoid.moveBy(dx, dy);
        } else {
            mTrapzoid.growBy(edge, dx, dy);
        }
        mDrawRect = computeLayout();
        invalidate();
    }

    // Returns the cropping rectangle in image space.
    public Rect getCropRect() {
        return mTrapzoid.getBoundingRect();
    }

    public float[] getTrapezoid() {
        return mTrapzoid.getPoints();
    }
    public Rect getPerspectiveCorrectedBoundingRect(){
        return mTrapzoid.getPerspectiveCorrectedBoundingRect();
    }

    // Maps the cropping rectangle from image space to screen space.
    private Rect computeLayout() {
        return mTrapzoid.getBoundingRect(mMatrix);
    }

    public void invalidate() {
        mContext.invalidate();
    }

}
