package com.yiyanf.fang.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.yiyanf.fang.entity.Channel;
import com.yiyanf.fang.ui.fragment.BaseFragment;
import java.util.List;


/**
 * Created by Administrator on 2016/3/30.
 */
public class ChannelPagerAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> fragments;
    private List<Channel> mChannels;

    public ChannelPagerAdapter(FragmentManager fm, List<BaseFragment> fragments, List<Channel> channels) {
        super(fm);
        this.fragments = fragments;
        this.mChannels = channels;
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels == null ? "" : mChannels.get(position).Title;
    }

}
