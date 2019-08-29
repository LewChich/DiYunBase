package com.example.mybase.entity;

import java.io.Serializable;

/**
 * @author WZ
 * @date 2019/5/23.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class BaseWebViewData implements Serializable {
    public String title;
    public String content;

    public BaseWebViewData() {
    }

    public BaseWebViewData(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
