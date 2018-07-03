package com.yiyanf.fang.entity;

import java.io.Serializable;

/**
 * Created by Hition on 2017/11/2.
 */

public class SimpleVideo implements Serializable {

    private String path;

    private long during;

    private long size;

    private boolean isSelected = false;
    private String fileName;
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuring() {
        return during;
    }

    public void setDuring(long during) {
        this.during = during;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isSelected() {
        return this.isSelected;
    }
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
