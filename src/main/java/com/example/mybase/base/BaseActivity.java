package com.example.mybase.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.mybase.utils.AppManager;
import com.example.mybase.utils.MyLogger;
import com.example.mybase.utils.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author DIYUN
 * @date 2019/4/22.
 * description：activity基类
 */
public abstract class BaseActivity extends FragmentActivity {
    protected Unbinder bind;
    private QMUITipDialog loadingDialog;
    public Object mIntentData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(intiLayout());
        //绑定view
        bind = ButterKnife.bind(this);
        //设置状态栏字体黑色
        initToolBar(true);
        //传递数据
        initIntentData();
        //添加activity到集合
        AppManager.getAppManager().addActivity(this);
        //初始化控件
        initView();
        //设置数据
        initData();
    }

    public void initToolBar(boolean statusBarFontBlack) {
        //沉浸式
        QMUIStatusBarHelper.translucent(this);
        if (statusBarFontBlack) {
            //设置状态栏黑色字体与图标
            QMUIStatusBarHelper.setStatusBarLightMode(this);
        } else {
            //设置状态栏白色字体与图标
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        }
    }

    protected Activity getAty() {
        return this;
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int intiLayout();

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 设置数据
     */
    public void initData() {}

    /**
     * 标题返回箭头点击事件
     *
     * @param var1
     */
    public void back(View var1) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 显示进度条
     */
    protected void showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        loadingDialog =
                new QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).setTipWord("正在加载").create(false);
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    /**
     * 隐藏进度条
     */
    protected void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 传递数据初始化
     */
    private void initIntentData() {
        this.mIntentData = this.getIntent().getSerializableExtra("Data");
    }


    public Object getIntentData() {
        return this.mIntentData;
    }

    protected void startAct(Activity act, Class cls) {
        this.startAct(act, cls, (Object) null, -1);
    }

    public void startAct(Activity act, Class cls, Object obj) {
        this.startAct(act, cls, obj, -1);
    }

    protected void startAct(Activity act, Class cls, Object obj, int requestCode) {
        AppManager.startActivity(act, cls, obj, requestCode);
    }

    /**
     * //弹出吐司
     *
     * @param msg         内容
     * @param requestCode 弹出框样式
     */
    protected void showToast(final String msg, final int requestCode) {
        ToastUtils.show(this, msg, requestCode);
    }

    /**
     * 打印日志
     *
     * @param msg
     */
    protected void showLog(final String msg) {
        MyLogger.dLog().d(msg);
    }
}
