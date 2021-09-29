package com.ext.priority.window;

import android.app.Activity;

import androidx.fragment.app.FragmentManager;

/**
 * 窗口约定规则
 */
public interface IWindow {

    String getClassName();

    /**
     * 弹窗展示
     */
    void show(Activity activity, FragmentManager manager);

    /**
     * 弹窗关闭
     */
    void dismiss();

    /**
     * 是否在显示
     *
     * @return
     */
    boolean isShowing();

    /**
     * 设置窗口关闭监听
     */
    void setOnWindowDismissListener(OnWindowDismissListener listener);

}
