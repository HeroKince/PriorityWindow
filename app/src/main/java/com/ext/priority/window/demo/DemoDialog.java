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
    /**
     * 是否被挤出（每个实现DialogManager.Dialog的窗口类都需要新建该变量）
     */
    private boolean isCrowdOut;

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

    /**
     * 每个实现DialogManager.Dialog的窗口类都需要实现该
     * 方法告诉DialogManager是否可展示此窗口（比如有些窗
     * 口只在页面的某个tab下才能展示）
     */
    @Override
    public boolean isCanShow() {
        return true;
    }

}