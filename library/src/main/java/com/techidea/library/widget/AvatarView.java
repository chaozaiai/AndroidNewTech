package com.techidea.library.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by sam.zhang on 2017/7/25.
 */

public class AvatarView extends AppCompatImageView {

    private static final String TAG = AvatarView.class.getSimpleName();

    /**
     * 图片背景
     */
    private Bitmap backgroundBmp;
    /**
     * 显示的图片
     */
    private Bitmap bitmap;
    private int viewWidth;
    private int viewHeight;

    public AvatarView(Context context) {
        this(context, null, 0);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setScaleType(ScaleType.CENTER_CROP);

    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }


    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
    }

    private void setBackgroundBmp() {
        if (null == getBackground()) {
            throw new IllegalArgumentException(String.format("background is null."));
        } else {
            backgroundBmp = getBimapFromDrawable(getBackground());
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null && backgroundBmp != null) {
            canvas.drawBitmap(createImage(), 0, 0, null);
        }
    }

    private Bitmap createImage() {
        backgroundBmp = getCenterCropBitmap(backgroundBmp, viewWidth, viewHeight);
        bitmap = getCenterCropBitmap(bitmap, viewWidth, viewHeight);

        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap finalBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawBitmap(backgroundBmp, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, (viewWidth - bmpWidth) / 2, (viewHeight - bmpHeight) / 2, paint);
        return finalBitmap;
    }

    private Bitmap getCenerInsideBimap(Bitmap src, float sideLength) {
        float srcWidth = src.getWidth();
        float srcHeight = src.getHeight();
        float scaleWidth = 0;
        float scaleHeight = 0;

        if (srcWidth > srcHeight) {
            scaleWidth = sideLength;
            scaleHeight = (sideLength / srcWidth) * srcHeight;
        } else if (srcWidth < srcHeight) {
            scaleWidth = (sideLength / srcHeight) * srcWidth;
            scaleHeight = sideLength;
        } else {
            scaleWidth = scaleHeight = sideLength;
        }
        return Bitmap.createScaledBitmap(src, (int) scaleWidth, (int) scaleHeight, false);
    }

    private Bitmap getCenterInsideBitmap(Bitmap src, float recWidth, float recHeight) {
        float srcRatio = ((float) src.getWidth()) / src.getHeight();
        float rectRadio = recWidth / recHeight;
        if (srcRatio < rectRadio) {
            return getCenerInsideBimap(src, recHeight);
        } else {
            return getCenerInsideBimap(src, recWidth);
        }
    }

    private Bitmap getCenterCropBitmap(Bitmap src, float rectWidth, float rectHeight) {
        float srcRatio = ((float) src.getWidth()) / src.getHeight();
        float rectRadio = rectWidth / rectHeight;
        if (srcRatio < rectRadio) {
            //当原图的宽比高 比例小时，使用rectWidth srcHeight
            return Bitmap.createScaledBitmap(src, (int) rectWidth,
                    (int) ((rectWidth / src.getWidth()) * src.getHeight()), false);
        } else {
            return Bitmap.createScaledBitmap(src, (int) ((rectHeight / src.getHeight())
                    * src.getWidth()), (int) rectHeight, false);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = getWidth();
        viewHeight = getHeight();
        bitmap = getBimapFromDrawable(getDrawable());
        setBackgroundBmp();
    }

    private Bitmap getBimapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

}
