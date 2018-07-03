package com.yiyanf.fang.util;

import android.app.Activity;
import android.os.Bundle;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.ResCheckVersion;
import com.yiyanf.fang.entity.BaseResponse;
import com.yiyanf.fang.presenter.imp.CheckVersionPresenter;
import com.yiyanf.fang.ui.fragment.CheckUpdateDialogFragment;
import com.yiyanf.fang.view.IView;


/**
 * Created by Hition on 2017/5/8.
 */

public class CheckVersionUtils implements IView<ResCheckVersion> {

    private Activity mContext;
    private CheckVersionPresenter presnter;
    private boolean isNeedShowNewerToast = false;

    public CheckVersionUtils(){
        presnter = new CheckVersionPresenter(this);
    }

    public void checkUpdate(Activity context,boolean isNeedShowNewerToast){
        this.mContext = context;
        this.isNeedShowNewerToast = isNeedShowNewerToast;
        presnter.checkVersion(AppVersionUtil.getPackageName(mContext),AppVersionUtil.getVersionCode(mContext));
    }

    /**
     * 显示更新提示对话框
     *
     */
    private void showUpdateDialog(String versionName,String downloadUrl,String updateLog){
        CheckUpdateDialogFragment dialogFragment = new CheckUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString("version_name", versionName);
        args.putString("download_url", downloadUrl);
        args.putString("update_desc", updateLog);
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(false);
        if (dialogFragment.isAdded()){
            dialogFragment.dismiss();
        }else{
            dialogFragment.show(mContext.getFragmentManager(), "");
        }
    }

    @Override
    public void fillData(BaseResponse<ResCheckVersion> data, int flag) {
        if(flag == FangConstants.CHECK_VERSION_UPDATE){
            if(0 == data.getReturnValue()){
                // 已经是最新版本，无需更新
                if(isNeedShowNewerToast){
                    ToastUtils.show(mContext, R.string.version_newer_tips);
                }
            }else if(1 == data.getReturnValue()){
                // 发现新版本
                ResCheckVersion newVersion = data.getReturnData();
                if(null != newVersion){
                    String versionName =  newVersion.getVersionname();
                    String downloadUrl = newVersion.getDownloadurl();
                    String updateLog = newVersion.getUpdatelog();

                    showUpdateDialog(versionName,downloadUrl,updateLog);
                }
            }
        }

    }

    @Override
    public void showFailedView(int flag) {

    }
}
