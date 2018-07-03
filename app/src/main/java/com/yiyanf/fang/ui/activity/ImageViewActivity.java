package com.yiyanf.fang.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.IMFileUtil;
import com.yiyanf.fang.util.ToastUtils;
import java.io.IOException;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageViewActivity extends Activity {

    @Bind(R.id.image)
    ImageView imageView;
    @Bind(R.id.root)
    RelativeLayout rootView;

    public static void startActivity(Context context, String filename) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtra("file_name", filename);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String fileName = getIntent().getStringExtra("file_name");
        if(fileName.startsWith("http://")){
            // 网络图片
            ImageLoader.load(this,fileName,imageView);
        }else{
            // 缓存中图片
            Bitmap bitmap = getImage(IMFileUtil.getCacheFilePath(fileName));
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Bitmap getImage(String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int reqWidth, reqHeight, width = options.outWidth, height = options.outHeight;
        if (width==0||height==0){
            ToastUtils.show(this, R.string.file_not_found);
            return null;
        }
        if (width > height) {
            reqWidth = getWindowManager().getDefaultDisplay().getWidth() / 2;
            reqHeight = (reqWidth * height) / width;
        } else {
            reqHeight = getWindowManager().getDefaultDisplay().getHeight() / 2;
            reqWidth = (width * reqHeight) / height;
        }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        try {
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            Matrix mat = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            if (bitmap == null) {
                ToastUtils.show(this, R.string.file_not_found);
                return null;
            }
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    mat.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    mat.postRotate(180);
                    break;
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        } catch (IOException e) {
            return null;
        }
    }
}
