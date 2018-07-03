package com.yiyanf.fang.eventbus;


/**
 * 视频分类event
 * Created by Hition on 2017/12/18.
 */

public class CategoryEvent {

    private int label;

    private int categoryId;

    private String categoryName;

    public CategoryEvent(int label,int categoryId, String categoryName) {
        this.label = label;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public CategoryEvent(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getLabel() {
        return label;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
