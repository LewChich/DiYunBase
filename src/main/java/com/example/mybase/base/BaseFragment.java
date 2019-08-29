package com.example.mybase.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mybase.utils.AppManager;
import com.example.mybase.utils.MyLogger;
import com.example.mybase.utils.ToastUtils;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author WZ
 * @date 2019/5/7.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public abstract class BaseFragment extends Fragment {
    private View mRootView;
    private Unbinder mUnbinder;
    protected Context mContext;
    private QMUITipDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == mRootView) {
            mRootView = inflater.inflate(intiLayout(), null);
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, mRootView);
        onViewReallyCreated(mRootView);
    }

    /**
     * 设置布局
     *
     * @return
     */
    protected abstract int intiLayout();

    /**
     * 设置数据
     *
     * @param view
     */
    protected abstract void onViewReallyCreated(View view);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    protected Fragment getFragment() {
        return this;
    }

    protected void startAct(Fragment act, Class cls) {
        this.startAct(act, cls, null, -1);
    }

    protected void startAct(Fragment act, Class cls, Object obj) {
        this.startAct(act, cls, obj, -1);
    }

    protected void startAct(Fragment act, Class cls, Object obj, int requestCode) {
        AppManager.startActivityForFrg(act, cls, obj, requestCode);
    }

    /**
     * 显示进度条
     */
    protected void showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        loadingDialog =
                new QMUITipDialog.Builder(mContext).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).setTipWord("正在加载").create(false);
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
     * //弹出吐司
     *
     * @param msg         内容
     * @param requestCode 弹出框样式
     */
    protected void showToast(final String msg, final int requestCode) {
        ToastUtils.show(mContext, msg, requestCode);
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
