package com.ext.priority.window.demo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.ext.priority.window.IWindow;
import com.ext.priority.window.OnWindowDismissListener;
import com.ext.priority.window.OnWindowShowListener;

/**
 * 窗口例子类
 */
public class DemoDialog extends AlertDialog implements IWindow {

    private OnWindowDismissListener onWindowDismissListener;

    protected DemoDialog(@NonNull Context context) {
        super(context);
    }

    protected DemoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DemoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show(Activity activity) {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void setOnWindowDismissListener(OnWindowDismissListener listener) {
        onWindowDismissListener = listener;
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onWindowDismissListener.onDismiss();
            }
        });
    }

    @Override
    public void setOnWindowShowListener(OnWindowShowListener listener) {
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                listener.onShow();
            }
        });
    }

}