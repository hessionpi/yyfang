package com.yiyanf.fang.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import com.yiyanf.fang.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片转换工具类
 *
 * Created by Hition on 2017/10/26.
 */

public class BitmapUtils {

    public static Bitmap textAsBitmap(Context mContext,String text, float textSize) {
        TextPaint textPaint = new TextPaint();
        // textPaint.setARGB(0x31, 0x31, 0x31, 0);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(textSize);
        StaticLayout layout = new StaticLayout(text, textPaint, 5,Layout.Alignment.ALIGN_NORMAL, 0.2f, 0.0f, true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 120,layout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(ContextCompat.getColor(mContext, R.color.transparent));
        layout.draw(canvas);
        LogUtil.d("textAsBitmap",String.format("1:%d %d", layout.getWidth(), layout.getHeight()));
        return bitmap;
    }

    public static Bitmap decodeUriAsBitmap(Context mContext,Uri uri){
        Bitmap bitmap ;
        try {
            bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 压缩图片尺寸
     * @param bitmap    原图片
     */
    public static Bitmap compress(Bitmap bitmap,float sx,float sy){
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy);
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
        return bm;
    }

    /**
     * 将bitmap 进行压缩后保存成文件
     * @param bitmap            原图
     * @param dirname           文件名
     * @return 保存成功后文件名
     */
    public static String bitmap2File(Bitmap bitmap,String dirname,boolean scale){
        if(scale){
            Matrix matrix = new Matrix();
            matrix.setScale(0.4f, 0.4f);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
        }

        File dirFile = new File(Environment.getExternalStorageDirectory() + dirname);//设置保存路径
        if (!dirFile .exists()) {              //如果不存在，那就建立这个文件夹
            dirFile .mkdirs();
        }
        File avaterFile = new File(dirFile, System.currentTimeMillis() + ".jpg");//设置文件名称
        try {
            FileOutputStream fos = new FileOutputStream(avaterFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return avaterFile.getAbsolutePath();
    }



}
