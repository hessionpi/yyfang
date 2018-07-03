package com.yiyanf.fang.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.Building;
import com.yiyanf.fang.ui.adapter.recycleadapter.BaseViewHolder;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class AuthBuildingAdapter extends XMBaseAdapter<String> {
    List<Integer> buildingsid;
    List<Building> buildings;

    public AuthBuildingAdapter(Context context) {
        super(context);
    }

    public void setBuildings(List<Integer> buildingsid, List<Building> buildings) {
        this.buildingsid = buildingsid;
        this.buildings = buildings;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new AuthBuildingHolder(parent, R.layout.item_recyclerview_auth_building);
    }

    private class AuthBuildingHolder extends BaseViewHolder<String> {
        TextView tv_building;
        TextView delete;

        AuthBuildingHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            tv_building = $(R.id.tv_building);
            delete = $(R.id.delete);

        }

        @Override
        public void setData(final String data) {
            tv_building.setText(data);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  buildingsid.remove();
                    remove(data);
                    for (int i = 0; i < buildings.size(); i++) {
                        if (buildings.get(i).getBuildingname().equals(data)) {
                            buildingsid.remove(i);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }

    }
}

