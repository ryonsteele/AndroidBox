package android.ryons.com.bwtiffimaging.imaging;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.Rotation;

public class ZoomableGPUImageView extends GPUImageView implements ScaleGestureDetector.OnScaleGestureListener {

    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private static final String TAG = "ZoomLayout";
    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 4.0f;

    private Mode mode = Mode.NONE;
    private float scale = 1.0f;
    private float lastScaleFactor = 0f;

    // Where the finger first  touches the screen
    private float startX = 0f;
    private float startY = 0f;

    // How much to translate the canvas
    private float dx = 0f;
    private float dy = 0f;
    private float prevDx = 0f;
    private float prevDy = 0f;

    public ZoomableGPUImageView(Context context) {
        super(context);
        init(context);
//        getGPUImage().setGLSurfaceView(new ZoomableGPUImageGLSurfaceView(context));
    }

    public ZoomableGPUImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
//        getGPUImage().setGLSurfaceView(new ZoomableGPUImageGLSurfaceView(context, attrs));
    }

//    public ZoomableGPUImageView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init(context);
//    }

//    public class ZoomableGPUImageGLSurfaceView extends GLSurfaceView {
//        public ZoomableGPUImageView.Size mForceSize = null;
//
//        public ZoomableGPUImageGLSurfaceView(Context context) {
//            super(context);
//            setRenderMode(RENDERMODE_CONTINUOUSLY);
//        }
//
//        public ZoomableGPUImageGLSurfaceView(Context context, AttributeSet attrs) {
//            super(context, attrs);
//            setRenderMode(RENDERMODE_CONTINUOUSLY);
//        }
//
//        @Override
//        public void setRenderer(Renderer renderer) {
//            super.setRenderer(renderer);
//        }
//
//        @Override
//        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            if (mForceSize != null) {
//                super.onMeasure(MeasureSpec.makeMeasureSpec(mForceSize.width, MeasureSpec.EXACTLY),
//                        MeasureSpec.makeMeasureSpec(mForceSize.height, MeasureSpec.EXACTLY));
//            } else {
//                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            }
//        }
//    }
//
//    public static class Size extends GPUImageView.Size {
//        int width;
//        int height;
//
//        public Size(int width, int height) {
//            super(width, height);
//            this.width = width;
//            this.height = height;
//        }
//    }

    private void init(Context context) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "DOWN");
                        if (scale > MIN_ZOOM) {
                            mode = Mode.DRAG;
                            startX = motionEvent.getX() - prevDx;
                            startY = motionEvent.getY() - prevDy;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == Mode.DRAG) {
                            dx = motionEvent.getX() - startX;
                            dy = motionEvent.getY() - startY;
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mode = Mode.ZOOM;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = Mode.DRAG;
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "UP");
                        mode = Mode.NONE;
                        prevDx = dx;
                        prevDy = dy;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);

                Log.i(TAG, "Mode: " + mode.name());
                if ((mode == Mode.DRAG && scale >= MIN_ZOOM) || mode == Mode.ZOOM) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = (child().getWidth() - (child().getWidth() / scale)) / 2 * scale;
                    float maxDy = (child().getHeight() - (child().getHeight() / scale))/ 2 * scale;
                    dx = Math.min(Math.max(dx, -maxDx), maxDx);
                    dy = Math.min(Math.max(dy, -maxDy), maxDy);
                    Log.i(TAG, "Width: " + child().getWidth() + ", scale " + scale + ", dx " + dx
                            + ", max " + maxDx);
                    applyScaleAndTranslation();
                }

                return true;
            }
        });
    }

    // ScaleGestureDetector

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleBegin");
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleDetector) {
        float scaleFactor = scaleDetector.getScaleFactor();
        Log.i(TAG, "onScale" + scaleFactor);
        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
            scale *= scaleFactor;
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
            lastScaleFactor = scaleFactor;
        } else {
            lastScaleFactor = 0;
        }
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleEnd");
    }

    private void applyScaleAndTranslation() {
        Log.i(TAG, "applyScaleAndTranslation: scale="+ scale);

        GLSurfaceView myChild = child();

//        this.getGPUImage().setScaleType(GPUImage.ScaleType.CENTER_CROP);
//        this.getGPUImage().setScaleType(GPUImage.ScaleType.CENTER_INSIDE);

////        child().setPivotX(child().getWidth()/2);
////        child().setPivotY(child().getHeight()/2);
        myChild.setScaleX(scale);
        myChild.setScaleY(scale);

//        myChild.setMinimumWidth((int) (myChild.getWidth() * scale));
//        myChild.setMinimumHeight((int) (myChild.getHeight() * scale));

////        ((View)myChild.getParent()).setScaleX(scale);
////        ((View)myChild.getParent()).setScaleY(scale);

//        this.setRatio(scale);
//        this.requestRender();

//        this.forceLayout();
//        myChild.forceLayout();

//        int height = this.getHeight();
//        int width = this.getWidth();

//        ViewGroup.LayoutParams params = myChild.getLayoutParams();
//        ViewGroup.LayoutParams params = this.getLayoutParams();
////        ViewGroup.LayoutParams params = this.getGPUImage().
//
//        params.height = (int) (height * scale);
//        params.width = (int) (width * scale);
//
//////        myChild.setLayoutParams(params);
//////        this.setLayoutParams(params);
//////        myChild.requestLayout();
////        if (this.getFilter() != null) {
////            this.setFilter(this.getFilter());
////        }

/*
        this.mForceSize = new Size(
                (int) (myChild.getWidth() * scale),
                (int) (myChild.getHeight() * scale));
*/

//        this.mForceSize = new Size(params.width, params.height);

        myChild.setTranslationX(dx);
        myChild.setTranslationY(dy);
    }

    private GLSurfaceView /*View*/ child() {
        return (GLSurfaceView) getChildAt(0);
//        return this;
    }
}
