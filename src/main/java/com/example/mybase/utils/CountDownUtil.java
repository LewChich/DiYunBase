package com.example.mybase.utils;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public  class CountDownUtil extends CountDownTimer {

    private TextView tv;//
    public static final long COUNT_DOWN_TIME = 60000;
    public static final long INTERVAL_TIME = 1000;

    /**
     *
     * millisInFuture
     *          倒计时时间
     * countDownInterval
     *          间隔
     * @param tv
     *          控件
     */
    public CountDownUtil(TextView tv) {
        super(COUNT_DOWN_TIME, INTERVAL_TIME);
        this.tv = tv;

    }

    @SuppressLint("NewApi")
    @Override
    public void onTick(long millisUntilFinished) {
        //设置时间
        tv.setText(millisUntilFinished/1000+"s");
        //这里接收的是毫秒值,当然,我们要将他格式化一下
        tv.setClickable(false);
    }

    @SuppressLint("NewApi")
    @Override
    public void onFinish() {
        tv.setClickable(true);
        tv.setText("重新获取");
    }

}
