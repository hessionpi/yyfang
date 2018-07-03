package com.yiyanf.fang.ui.widget.pull2refresh;

/**
 * Created by Hition on 2017/2/10.
 */
public interface RefreshView {
    void setPercent(float percent, boolean invalidate);

    void offsetTopAndBottom(int offset);

    void stop();

    void start();
}
