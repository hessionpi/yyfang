package com.yiyanf.fang.entity;

import java.io.Serializable;

/**
 * 仅包含id 和name 的entity
 *
 * Created by Hition on 2017/12/15.
 */

public class SimpleText implements Serializable {

    private String textId;

    private String text;

    public SimpleText(String textId, String text) {
        this.textId = textId;
        this.text = text;
    }

    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
