package com.yiyanf.fang.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.ResGetCOSSign;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.presenter.imp.UploadPresenter;
import com.yiyanf.fang.util.BitmapUtils;
import com.yiyanf.fang.util.PixelUtil;
import com.yiyanf.fang.view.IView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 所有需要选择图片的activity都继承次父类
 * <p>
 * Created by Hition on 2018/1/2.
 */

public abstract class PictureActivity extends BaseActivity implements View.OnClickListener, IView {

    Dialog dialog;
    private Uri photoUri = null;
    // 图片最终的输出文件的 Uri
    private Uri photoOutputUri = null;


    protected static final int CAMERA = 2;
    protected static final int ALBUM = 3;
    protected static final int CROP_BITMAP = 8;

    private UploadPresenter uploadPresenter;
    boolean isCrop = true;
    String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uploadPresenter = new UploadPresenter(this);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            // 拍照权限
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            // 读取存储权限
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            // 写入存储权限
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), 100);
                return false;
            }
        }
        return true;
    }

    protected void showPhotoDialog(Boolean isCrop) {
        this.isCrop = isCrop;
        if (checkPermission()) {
            dialog = createSelectPhotoDialog();
            dialog.show();
        }
    }

    private Dialog createSelectPhotoDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_select_photo, null);

        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(layout);

        Window dialogWindow = dialog.getWindow();//获取window
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView camera = ButterKnife.findById(layout, R.id.camera);
        TextView photo = ButterKnife.findById(layout, R.id.photo);
        TextView cancel = ButterKnife.findById(layout, R.id.cancel);
        camera.setOnClickListener(this);
        photo.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera:
                startCamera();
                dialog.dismiss();
                break;

            case R.id.photo:
                choiceFromAlbum();
                dialog.dismiss();
                break;
            case R.id.cancel:
                dialog.dismiss();
                break;
        }
    }

    /**
     * 拍照
     */
    private void startCamera() {
        // 设置拍照得到的照片的储存目录，因为我们访问应用的缓存路径并不需要读写内存卡的申请权限，
        // 因此这里为了方便，将拍照得到的照片存在这个缓存目录中
     /*   File file = new File(getExternalCacheDir(), "image.jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 因 Android 7.0 开始，不能使用 file:// 类型的 Uri 访问跨应用文件，否则报异常，
        // 因此我们这里需要使用内容提供器，FileProvider 是 ContentProvider 的一个子类，
        // 我们可以轻松的使用 FileProvider 来在不同程序之间分享数据(相对于 ContentProvider 来说)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            photoUri = FileProvider.getUriForFile(this, "com.yiyanf.fang.fileprovider", file);
        } else {
            photoUri = Uri.fromFile(file); // Android 7.0 以前使用原来的方法来获取文件的 Uri
        }

        // 打开系统相机的 Action，等同于："android.media.action.IMAGE_CAPTURE"
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 设置拍照所得照片的输出目录
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takePhotoIntent, CAMERA);
*/
        File outputImage = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".png");
        try {
            if (outputImage.exists()) {
                outputImage.delete();//如果文件存在，则删除
            }
            outputImage.createNewFile();//创建一个新文件
        } catch (Exception e) {
            e.printStackTrace();
        }
        photoUri = Uri.fromFile(outputImage);
       // createFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent1.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent1, CAMERA);
    }

    /**
     * 从相册选取
     */
    private void choiceFromAlbum() {
        File outputImage = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".png");
        try {
            if (outputImage.exists()) {
                outputImage.delete();//如果文件存在，则删除
            }
            outputImage.createNewFile();//创建一个新文件
        } catch (Exception e) {
            e.printStackTrace();
        }
        photoUri = Uri.fromFile(outputImage);
        // 打开系统图库的 Action，等同于: "android.intent.action.GET_CONTENT"
        Intent choiceFromAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        // 设置数据类型为图片类型
        choiceFromAlbumIntent.setType("image/*");
        startActivityForResult(choiceFromAlbumIntent, ALBUM);

    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri inputUri) {
        // 调用系统裁剪图片的 Action
        Intent cropPhotoIntent = new Intent("com.android.camera.action.CROP");
        // 设置数据Uri 和类型
        cropPhotoIntent.setDataAndType(inputUri, "image/*");
        // 授权应用读取 Uri，这一步要有，不然裁剪程序会崩溃
        cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        cropPhotoIntent.putExtra("crop", "true");
        cropPhotoIntent.putExtra("aspectX", 1);
        cropPhotoIntent.putExtra("aspectY", 1);
        cropPhotoIntent.putExtra("outputX", PixelUtil.getScreenWidth(this));
        cropPhotoIntent.putExtra("outputY", PixelUtil.getScreenWidth(this));
        // 设置图片的最终输出目录
        photoOutputUri = Uri.parse("file:////sdcard/upload_img.jpg");
        cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri);
        cropPhotoIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        cropPhotoIntent.putExtra("noFaceDetection", true);
        startActivityForResult(cropPhotoIntent, CROP_BITMAP);
    }


    protected abstract void uploadPhoto(String cosSign, String path);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            // 通过返回码判断是哪个应用返回的数据
            switch (requestCode) {
                // 拍照
                case CAMERA:

                    if (isCrop) {
                        cropPhoto(photoUri);
                    } else {
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String bitmap2File = BitmapUtils.bitmap2File(bitmap, photoUri.getPath(),true);
                       // imagePath =  FileUtils.saveBitmap(bitmap,System.currentTimeMillis() + ".png");
                        photoOutputUri = Uri.parse(bitmap2File);
                        if (photoOutputUri != null) {
                            uploadPresenter.getCOSSign();
                        }
                    }
                    break;
                // 相册选择
                case ALBUM:
                    if (isCrop) {
                        cropPhoto(data.getData());
                    } else {
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String bitmap2File = BitmapUtils.bitmap2File(bitmap, photoUri.getPath(),true);
                        // imagePath =  FileUtils.saveBitmap(bitmap,System.currentTimeMillis() + ".png");
                        photoOutputUri = Uri.parse(bitmap2File);
                        if (photoOutputUri != null) {
                            uploadPresenter.getCOSSign();
                        }
                      //  photoOutputUri = Uri.parse(uri2filePath(data.getData(), this));
                        uploadPresenter.getCOSSign();
                    }

                    break;
                // 裁剪图片
                case CROP_BITMAP:
                    if (data != null) {
                        // 先获取cos签名后上上传
                        uploadPresenter.getCOSSign();
                    }
                    break;
            }
        }
    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        if (0 == data.getReturnValue()) {
            switch (flag) {
                case FangConstants.COS_SIGN:
                    if (FangConstants.RETURN_VALUE_OK == data.getReturnValue()) {
                        if (data.getReturnData() instanceof ResGetCOSSign) {
                            ResGetCOSSign returnData = (ResGetCOSSign) data.getReturnData();
                            String sign = returnData.getSign();

                            /*Bitmap bitmap = BitmapUtils.decodeUriAsBitmap(this, imageUri);
                            String avataFilePath = BitmapUtils.bitmap2File(bitmap,File.separator + FangConstants.DIR_PATH + File.separator + "photo",false);*/
                            if (photoOutputUri != null) {
                                uploadPhoto(sign, photoOutputUri.getPath());
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void showFailedView(int flag) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != uploadPresenter) {
            uploadPresenter.onUnsubscribe();
        }
    }
}
