package com.techidea.library.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by sam on 2018/1/31.
 */

public class BitmapUtil {

    //bitmap to base64 post 提交图片
    public static String bitmapToBase64(Bitmap bitmap) {
        if(bitmap == null) {
            return null;
        } else {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                byte[] buffer = out.toByteArray();
                byte[] encode = Base64.encode(buffer, 0);
                return new String(encode);
            } catch (IOException var4) {
                var4.printStackTrace();
                return null;
            }
        }
    }
}
