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
     * 是否展示
     * @return
     */
    boolean isShowing();

    /**
     * 设置窗口关闭监听
     */
    void setOnWindowDismissListener(OnWindowDismissListener listener);

}
