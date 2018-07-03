package com.yiyanf.fang.ui.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.ResGetCOSSign;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.presenter.imp.UploadPresenter;
import com.yiyanf.fang.presenter.imp.UserPresenter;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.CosUtil;
import com.yiyanf.fang.util.FileUtils;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.ToastUtils;
import com.yiyanf.fang.view.IView;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 完善资料
 */
public class PerfectDataActivity extends PictureActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, IView {

    @Bind(R.id.iv_camera)
    ImageView ivCamera;
    @Bind(R.id.cet_nickname)
    EditText cetNickname;
    @Bind(R.id.rb_man)
    RadioButton rbMan;
    @Bind(R.id.rb_woman)
    RadioButton rbWoman;
    @Bind(R.id.rg_sex)
    RadioGroup rgSex;
    @Bind(R.id.bt_submit)
    Button btSubmit;
    int headpic;
    private CosUtil uploadUtils;

    String httpUrl;
    UserPresenter presenter;
    int sex;
    boolean isDefault = true;
    String nickName;
    private UploadPresenter uploadPresenter;
    //private boolean isMyNackName;
    String imagePath;
    private static final String SPECIAL_CHARACTERS = "[^\\u4e00-\\u9fa5a-zA-Z]";
    private static final Pattern NAME_PATTERN;

    static {
        NAME_PATTERN = Pattern.compile(SPECIAL_CHARACTERS);
    }
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PerfectDataActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_data);
        ButterKnife.bind(this);
        presenter = new UserPresenter(this);
        uploadPresenter = new UploadPresenter(this);
        uploadUtils = new CosUtil(this, new UploadListener());
        initView();
    }

    private void initView() {
        headpic = R.drawable.icon_man_head;
        LoginModel model = UserInfoCenter.getInstance().getLoginModel();
        if (model != null)
            cetNickname.setText(model.getNickname());
        // ImageLoader.loadTransformImage(this, UserInfoCenter.getInstance().getLoginModel().getHeadpic(), R.drawable.camera_img, R.drawable.camera_img, ivCamera, 0);
        ivCamera.setOnClickListener(this);
        rgSex.setOnCheckedChangeListener(this);
        btSubmit.setOnClickListener(this);
        cetNickname.addTextChangedListener(textWatcher);
    }



    private TextWatcher textWatcher = new TextWatcher() {
        private int editStart;
        private int editEnd;
        private int maxLen = 14;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = cetNickname.getSelectionStart();
            editEnd = cetNickname.getSelectionEnd();
            cetNickname.removeTextChangedListener(textWatcher);
            if (!TextUtils.isEmpty(cetNickname.getText())) {
                String etstring = cetNickname.getText().toString().trim();
                while (calculateLength(s.toString()) > maxLen) {
                    s.delete(editStart - 1, editEnd);
                    editStart--;
                    editEnd--;
                }
            }
            cetNickname.setText(s);
            cetNickname.setSelection(editStart);
            Matcher m = NAME_PATTERN.matcher(s);
            if (m.find()) {
              //  ToastUtils.(mContext, R.string.robot_name_error);
                editStart = cetNickname.getSelectionStart();
                s.delete(cetNickname.getSelectionEnd() - 1, cetNickname.getSelectionEnd());
                cetNickname.setText(s);
                cetNickname.setSelection(editStart - 1);
            }
            cetNickname.addTextChangedListener(textWatcher);
        }
    };

    private int calculateLength(String etstring) {
        char[] ch = etstring.toCharArray();
        int varlength = 0;
        for (int i = 0; i < ch.length; i++) {
            if ((ch[i] >= 0x2E80 && ch[i] <= 0xFE4F) || (ch[i] >= 0xA13F && ch[i] <= 0xAA40) || ch[i] >= 0x80) {
                varlength = varlength + 2;
            } else {
                varlength++;
            }
        }
        return varlength;
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.iv_camera:
                super.showPhotoDialog(true);
                break;

            case R.id.bt_submit:
                nickName = cetNickname.getText().toString();
                Boolean isUsername = true;
                //  if (isMyNackName) {
                // isUsername = RegularUtils.isUsername(nickName);
                // }
                if (nickName != null && !"".equals(nickName)) {
                    if (isDefault) {
                        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                                + getResources().getResourcePackageName(headpic) + "/"
                                + getResources().getResourceTypeName(headpic) + "/"
                                + getResources().getResourceEntryName(headpic));
                        //createFile();

                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            imagePath = FileUtils.saveBitmap(bitmap, System.currentTimeMillis() + ".png");
                            //   BitmapUtils.bitmap2File(bitmap, imageUri.getPath(),false);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (imagePath != null) {
                            uploadPresenter.getCOSSign();
                        }
                    } else {
                        presenter.updateUserinfo(httpUrl, nickName, sex,null,null);
                    }
                } else {
                    ToastUtils.show(this, "昵称不能为空！");
                }
                break;
        }
    }

 /*   void createFile() {
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
        // imageUri = Uri.fromFile(outputImage);
    }*/

    @Override
    protected void uploadPhoto(String cosSign, String path) {
        isDefault = false;
        uploadUtils.upLoad(cosSign, path, false);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) PerfectDataActivity.this.findViewById(radioButtonId);
        sex = 0;
        if (rb.getId() == R.id.rb_man) {
            sex = 0;
            headpic = R.drawable.icon_man_head;
            if (isDefault)
                ivCamera.setImageResource(R.drawable.icon_man_head_circle);
        } else {
            sex = 1;
            headpic = R.drawable.icon_woman_head;
            if (isDefault)
                ivCamera.setImageResource(R.drawable.icon_woman_head_circle);
        }
        LogUtil.v("您的性别是：" + sex);
    }

    @Override
    public void fillData(BaseResponse data, int flag) {
        super.fillData(data, flag);
        switch (flag) {
            case FangConstants.COS_SIGN:
                if (FangConstants.RETURN_VALUE_OK == data.getReturnValue()) {
                    if (data.getReturnData() instanceof ResGetCOSSign) {
                        ResGetCOSSign returnData = (ResGetCOSSign) data.getReturnData();
                        String sign = returnData.getSign();
                        // uploadPhoto(sign, imagePath);
                        uploadUtils.upLoad(sign, imagePath, false);
                        //  presenter.updateUserinfo(sign, nickName, sex);
                    }
                }
                break;
            case FangConstants.USER_PERFECT_DATA:
                if (FangConstants.RETURN_VALUE_OK == data.getReturnValue()) {
                    LoginModel loginModel= UserInfoCenter.getInstance().getLoginModel();
                    if (loginModel==null){
                        loginModel=new LoginModel();
                    }
                    loginModel.setNickname(cetNickname.getText().toString());
                    loginModel.setHeadpic(httpUrl);
                    loginModel.setSex(sex);
                    UserInfoCenter.getInstance().setLoginBean(loginModel);
                    Toast.makeText(this, "完善资料成功", Toast.LENGTH_LONG).show();
                    MainActivity.startActivity(this);
                }
                break;
        }
    }

    @Override
    public void showFailedView(int flag) {

    }

    private class UploadListener implements CosUtil.OnUploadListener {

        @Override
        public void onSuccess(int code, String url) {

            httpUrl = url;

            if (isDefault) {
                presenter.updateUserinfo(httpUrl, nickName, sex,null,null);
            } else {
                ImageLoader.loadTransformImage(getApplicationContext(), url, R.drawable.icon_man_head_circle, R.drawable.icon_man_head_circle, ivCamera, 0);
            }
        }

        @Override
        public void onFailed(int errorCode, String msg) {
            // 头像上传COS失败
            LogUtil.e("hition==", msg + errorCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != presenter) {
            presenter.onUnsubscribe();
        }
    }
}
