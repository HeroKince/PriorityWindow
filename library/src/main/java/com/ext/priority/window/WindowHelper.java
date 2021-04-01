package com.ext.priority.window;

/**
 * Activity启动情况比较特殊，需要把数据保存起来
 */
public class WindowHelper {

    private boolean isActivityShow;
    private OnWindowDismissListener mActivityDismissListener;

    private static WindowHelper mDefaultInstance;

    private WindowHelper() {
    }

    /**
     * 获取弹窗管理者
     */
    public static WindowHelper getInstance() {
        if (mDefaultInstance == null) {
            synchronized (WindowHelper.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new WindowHelper();
                }
            }
        }
        return mDefaultInstance;
    }

    public OnWindowDismissListener getActivityDismissListener() {
        return mActivityDismissListener;
    }

    public void setActivityDismissListener(OnWindowDismissListener mActivityDismissListener) {
        this.mActivityDismissListener = mActivityDismissListener;
    }

    public boolean isActivityShow() {
        return isActivityShow;
    }

    public void setActivityShow(boolean activityShow) {
        isActivityShow = activityShow;
    }

    public void onDestroy() {
        mActivityDismissListener = null;
        isActivityShow = false;
    }

}
