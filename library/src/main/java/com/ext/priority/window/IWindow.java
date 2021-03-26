package com.ext.priority.window;

import android.app.Activity;

/**
 * 窗口约定规则
 */
public interface IWindow {

    /**
     * 弹窗展示
     */
    void show(Activity activity);

    /**
     * 弹窗关闭
     */
    void dismiss();

    /**
     * 设置窗口关闭监听
     */
    void setOnWindowDismissListener(OnWindowDismissListener listener);

    /**
     * 设置窗口展示监听
     */
    void setOnWindowShowListener(OnWindowShowListener listener);

}
