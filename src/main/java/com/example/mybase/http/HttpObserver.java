package com.example.mybase.http;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.example.mybase.Flag;
import com.example.mybase.utils.BaseEventData;
import com.example.mybase.utils.MyLogger;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.InterruptedIOException;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class HttpObserver<T> extends DisposableObserver<T> {
    private static final String TAG = "Request";
    private static Context sContext = null;
    private static QMUITipDialog loadingDialog;
    private HttpSuccessListener<T> mSuccessListener;
    private HttpErrorListener mErrorListener;

    public HttpObserver() {
    }

    public HttpObserver(HttpSuccessListener<T> successListener) {
        if (successListener == null) {
            throw new NullPointerException("HttpSuccessListener not null");
        } else {
            this.mSuccessListener = successListener;
        }
    }

    public HttpObserver(HttpSuccessListener<T> successListener, HttpErrorListener errorListener) {
        if (successListener == null) {
            throw new NullPointerException("HttpSuccessListener not null");
        } else if (errorListener == null) {
            throw new NullPointerException("HttpErrorListener not null");
        } else {
            this.mSuccessListener = successListener;
            this.mErrorListener = errorListener;
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public HttpObserver(Context context) {
        if (context == null) {
            throw new NullPointerException("Context not null");
        } else {
            WeakReference<Context> wc = new WeakReference(context);
            sContext = wc.get();
        }
    }

    public static <T> ObservableTransformer<T, T> schedulers() {

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> schedulers(Context context) {
        WeakReference<Context> wk = new WeakReference(context);
        sContext = (Context) wk.get();
        showLoadingDialog();
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 显示进度条
     */
    private static void showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        loadingDialog =
                new QMUITipDialog.Builder(sContext).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).setTipWord("正在加载").create();
        loadingDialog.setCancelable(false);
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void onError(Throwable e) {
        dismissLoadingDialog();
        if (e instanceof NetworkErrorException || e instanceof UnknownHostException || e instanceof ConnectException) {
            //连接地址或者断网
            Throwable da = new Throwable("请检查网络连接是否畅通");
            this.mErrorListener.error(da);
        } else if (e instanceof InterruptedIOException || e instanceof TimeoutException) {
            Throwable da = new Throwable("请求超时");
            this.mErrorListener.error(da);
        } else if (e instanceof ApiException) {
            EventBus.getDefault().post(new BaseEventData<>(Flag.Event.JUMP_LOGIN));
        } else if (this.mErrorListener != null) {
            this.mErrorListener.error(e);
        } else {
            MyLogger.dLog().e(new Object[]{e.getMessage()});
        }
    }

    @Override
    public void onComplete() {
        dismissLoadingDialog();
    }

    @Override
    public void onNext(T t) {
        this.mSuccessListener.success(t);
    }

    /**
     * 隐藏进度条
     */
    private void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

}
