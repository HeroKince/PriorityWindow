package com.ext.priority.window;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 弹窗管理（支持设置弹窗优先级）
 */
public class WindowTaskManager {
    private List<WindowWrapper> mWindows;

    private static WindowTaskManager mDefaultInstance;

    private WindowTaskManager() {
    }

    /**
     * 获取弹窗管理者
     */
    public static WindowTaskManager getInstance() {
        if (mDefaultInstance == null) {
            synchronized (WindowTaskManager.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new WindowTaskManager();
                }
            }
        }
        return mDefaultInstance;
    }

    /**
     * 添加弹窗
     *
     * @param windowWrapper 待添加的弹窗
     */
    public synchronized void add(Activity activity, WindowWrapper windowWrapper) {
        if (windowWrapper != null && windowWrapper.getWindow() != null) {
            if (mWindows == null) {
                mWindows = new ArrayList<>();
            }
            windowWrapper.getWindow().setOnWindowShowListener(new OnWindowShowListener() {
                @Override
                public void onShow() {
                    windowWrapper.setShowing(true);
                }
            });

            windowWrapper.getWindow().setOnWindowDismissListener(new OnWindowDismissListener() {
                @Override
                public void onDismiss() {
                    windowWrapper.setShowing(false);
                    mWindows.remove(windowWrapper);
                    showNext(activity);
                }
            });
            mWindows.add(windowWrapper);
        }
    }

    /**
     * 展示弹窗（优先级最高的Window）
     */
    public synchronized void show(Activity activity) {
        WindowWrapper windowWrapper = getMaxPriorityWindow();
        if (windowWrapper != null && windowWrapper.isCanShow()) {
            IWindow window = windowWrapper.getWindow();
            if (window != null) {
                window.show(activity);
            }
        }
    }

    /**
     * 展示弹窗
     *
     * @param windowWrapper 待展示的弹窗
     */
    public synchronized void show(Activity activity, WindowWrapper windowWrapper) {
        if (windowWrapper != null && windowWrapper.getWindow() != null) {
            WindowWrapper topShowWindow = getShowingWindow();
            if (topShowWindow == null) {
                /*获取优先级*/
                int priority = windowWrapper.getPriority();
                WindowWrapper maxPriorityWindow = getMaxPriorityWindow();
                if (maxPriorityWindow != null && windowWrapper.isCanShow()) {
                    if (priority >= maxPriorityWindow.getPriority()) {
                        windowWrapper.getWindow().show(activity);
                    }
                }
            }
        }
    }

    /**
     * 移除要展示的弹窗
     *
     * @param windowWrapper
     */
    public synchronized void remove(WindowWrapper windowWrapper) {
        if (windowWrapper != null && windowWrapper.getWindow() != null) {
            if (mWindows != null) {
                mWindows.remove(windowWrapper);
            }
        }
    }

    /**
     * 清除弹窗管理者
     */
    public synchronized void clear() {
        if (mWindows != null) {
            for (int i = 0, size = mWindows.size(); i < size; i++) {
                if (mWindows.get(i) != null) {
                    IWindow window = mWindows.get(i).getWindow();
                    if (window != null) {
                        window.dismiss();
                    }
                }
            }
            mWindows.clear();
        }
    }

    /**
     * 清除弹窗管理者
     *
     * @param dismiss 是否同时dismiss掉弹窗管理者维护的弹窗
     */
    public synchronized void clear(boolean dismiss) {
        if (mWindows != null) {
            if (dismiss) {
                for (int i = 0, size = mWindows.size(); i < size; i++) {
                    if (mWindows.get(i) != null) {
                        IWindow window = mWindows.get(i).getWindow();
                        if (window != null) {
                            window.dismiss();
                        }
                    }
                }
            }
            mWindows.clear();
        }
    }

    /**
     * 展示下一个优先级最大的Window
     */
    private synchronized void showNext(Activity activity) {
        WindowWrapper windowWrapper = getMaxPriorityWindow();
        if (windowWrapper != null && windowWrapper.isCanShow()) {
            if (windowWrapper.getWindow() != null) {
                windowWrapper.getWindow().show(activity);
            }
        }
    }

    /**
     * 获取当前栈中优先级最高的Window（优先级相同则返回后添加的弹窗）
     */
    private synchronized WindowWrapper getMaxPriorityWindow() {
        if (mWindows != null) {
            int maxPriority = -1;
            int position = -1;
            for (int i = 0, size = mWindows.size(); i < size; i++) {
                WindowWrapper windowWrapper = mWindows.get(i);
                if (i == 0) {
                    position = 0;
                    maxPriority = windowWrapper.getPriority();
                } else {
                    if (windowWrapper.getPriority() >= maxPriority) {
                        position = i;
                        maxPriority = windowWrapper.getPriority();
                    }
                }
            }
            if (position != -1) {
                return mWindows.get(position);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取当前处于show状态的弹窗
     */
    private synchronized WindowWrapper getShowingWindow() {
        if (mWindows != null) {
            for (int i = 0, size = mWindows.size(); i < size; i++) {
                WindowWrapper windowWrapper = mWindows.get(i);
                if (windowWrapper != null && windowWrapper.getWindow() != null && windowWrapper.isShowing()) {
                    return windowWrapper;
                }
            }
        }
        return null;
    }

}