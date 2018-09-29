package cn.com.amway.content.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.techidea.library.utils.DateUtil;

import java.io.ByteArrayOutputStream;

/**
 * Created by sam on 2018/7/6.
 */

public class BitmapEncodeUtils {

    private static final String TAG = BitmapEncodeUtils.class.getCanonicalName();

    public static String encodeBase64(String path) {
        Log.i(TAG, "base 64 start: "+DateUtil.getDateFormat(System.currentTimeMillis(), DateUtil.MESSAGE_FORMAT));
        String base64 = "";
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Log.d(TAG, "bitmap width: " + bitmap.getWidth() + " height: " + bitmap.getHeight());
        //convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        //base64 encode
        byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
        base64 = new String(encode);
//        StringBuffer stringBuffer = new StringBuffer(encode.toString());
        Log.i(TAG, "base 64 end: "+DateUtil.getDateFormat(System.currentTimeMillis(), DateUtil.MESSAGE_FORMAT));
        return base64;
    }
}
