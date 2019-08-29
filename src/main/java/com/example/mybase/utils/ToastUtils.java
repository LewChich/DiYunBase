package com.example.mybase.utils;

import android.content.Context;

import com.bravin.btoast.BToast;

/**
 * @author WZ
 * @date 2019/5/14.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class ToastUtils {
    //绿色
    public static int SUCCESS = 1;
    //红色
    public static int ERROR = 2;
    //蓝色
    public static int INFO = 3;
    //黄色
    public static int WARING = 4;
    //黑色
    public static int NORMAL = 5;


    public static void show(Context context, String message, int requestCode) {
        if (requestCode == SUCCESS) {
            BToast.success(context)
                  .text(message)
                  .show();
        } else if (requestCode == ERROR) {
            BToast.error(context)
                  .text(message)
                  .show();
        } else if (requestCode == INFO) {
            BToast.info(context)
                  .text(message)
                  .show();
        } else if (requestCode == WARING) {
            BToast.warning(context)
                  .text(message)
                  .show();
        } else if (requestCode == NORMAL) {
            BToast.normal(context)
                  .text(message)
                  .show();
        }
    }
}
