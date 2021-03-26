package com.ext.priority.window.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ext.priority.window.IWindow;
import com.ext.priority.window.OnWindowDismissListener;
import com.ext.priority.window.OnWindowShowListener;
import com.ext.priority.window.R;

public class DemoPopupWindow extends PopupWindow implements IWindow {

    private OnWindowShowListener mOnWindowShowListener;

    public DemoPopupWindow(Context context) {
        super(context);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_demo, null, false);
        setContentView(contentView);
    }

    @Override
    public void show(Activity activity) {
        showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        mOnWindowShowListener.onShow();
    }

    @Override
    public void dismiss() {
        dismiss();
    }

    @Override
    public void setOnWindowDismissListener(OnWindowDismissListener listener) {
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                listener.onDismiss();
            }
        });
    }

    @Override
    public void setOnWindowShowListener(OnWindowShowListener listener) {
        mOnWindowShowListener = listener;
    }

    @Override
    public boolean isCanShow() {
        return true;
    }

}
