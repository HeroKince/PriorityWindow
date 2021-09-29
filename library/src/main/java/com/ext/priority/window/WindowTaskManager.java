package com.ext.priority.window;

import android.app.Activity;

import androidx.fragment.app.FragmentManager;

import java.util.HashMap;

/**
 * 弹窗管理
 * <p>
 * - 按优先级顺序阻塞式显示各种类型弹窗，默认从最高优先级开始显示
 * - 只有上一个高优先级弹窗显示完或者取消显示，下一个低优先级弹窗才可以显示
 * - 指定显示某一个弹窗的前提是没有更高优先级的弹窗需要显示
 * - 在显示一个弹窗之前需要判断是否能够或者需要显示
 * - 根据优先级去查找指定的弹窗，优先级相当于唯一ID
 * - 显示过的弹窗可以根据业务逻辑再次显示
 * - 优先级相同的弹窗则后添加的弹窗先显示
 */
public class WindowTaskManager {

    private HashMap<String, WindowWrapper> mWindows;

    private static WindowTaskManager mDefaultInstance;

    private boolean enable = true;

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
     * <p>
     * 1.添加立即需要显示的弹窗
     * 2.添加需要延迟显示的弹窗
     *
     * @param windowWrapper 待显示的弹窗
     */
    public synchronized void addWindow(WindowWrapper windowWrapper) {
        if (windowWrapper != null) {
            if (mWindows == null) {
                mWindows = new HashMap<>();
            }

            if (hasAddWindow(windowWrapper)) {
                return;
            }
            mWindows.put(windowWrapper.getWindowName(), windowWrapper);
        }
    }

    /**
     * 弹窗满足展示条件，准备显示
     *
     * @param activity
     * @param manager
     * @param windowWrapper
     */
    public synchronized void showWindow(Activity activity, FragmentManager manager, WindowWrapper windowWrapper) {
        IWindow window = windowWrapper.getWindow();
        if (window != null) {
            addWindow(windowWrapper);
            if (enable) {
                show(activity, manager, windowWrapper);
            }
        }
    }

    /**
     * 标记需要显示的弹窗
     *
     * @param windowName
     */
    public synchronized void enableWindow(String windowName) {
        WindowWrapper windowWrapper = getTargetWindow(windowName);
        if (windowWrapper != null) {
            windowWrapper.setCanShow(true);
        }
    }

    /**
     * 移除不需要显示弹窗
     *
     * @param windowName
     */
    public synchronized void disableWindow(String windowName) {
        WindowWrapper windowWrapper = getTargetWindow(windowName);
        if (windowWrapper != null) {
            if (mWindows != null) {
                mWindows.remove(windowName);
            }
        }
    }

    /**
     * 展示弹窗
     * 当有阻塞弹窗时调用此方法，从优先级最高的Window开始显示
     */
    public synchronized void continueShow(Activity activity, FragmentManager manager) {
        if (!enable) {
            return;
        }
        WindowWrapper windowWrapper = getMaxPriorityWindow();
        if (windowWrapper != null && !windowWrapper.isWindowShow() && windowWrapper.isCanShow()) {
            IWindow window = windowWrapper.getWindow();
            if (window != null && isActivityAlive(activity)) {
                windowWrapper.setWindowShow(true);
                windowWrapper.getWindow().setOnWindowDismissListener(() -> {
                    if (windowWrapper.getWindow().getClassName().equalsIgnoreCase(windowWrapper.getWindowName())) {
                        windowWrapper.setWindowShow(false);
                        mWindows.remove(windowWrapper.getWindowName());
                        if (windowWrapper.isAutoShowNext()) {
                            showNext(activity, manager);
                        }
                    }
                });
                window.show(activity, manager);
            }
        }
    }

    /**
     * 清除弹窗管理者
     */
    public synchronized void clear() {
        if (mWindows != null) {
            for (HashMap.Entry<String, WindowWrapper> entry : mWindows.entrySet()) {
                WindowWrapper windowWrapper = entry.getValue();
                if (windowWrapper != null) {
                    IWindow window = windowWrapper.getWindow();
                    if (window != null && window.isShowing()) {
                        window.dismiss();
                    }
                }
            }
            mWindows.clear();
        }
        WindowHelper.getInstance().onDestroy();
    }

    /**
     * 清除弹窗管理者
     *
     * @param dismiss 是否同时dismiss掉弹窗管理者维护的弹窗
     */
    public synchronized void clear(boolean dismiss) {
        if (mWindows != null) {
            if (dismiss) {
                for (HashMap.Entry<String, WindowWrapper> entry : mWindows.entrySet()) {
                    WindowWrapper windowWrapper = entry.getValue();
                    if (windowWrapper != null) {
                        IWindow window = windowWrapper.getWindow();
                        if (window != null && window.isShowing()) {
                            window.dismiss();
                        }
                    }
                }
            }
            mWindows.clear();
        }
        WindowHelper.getInstance().onDestroy();
    }

    public boolean hasShowingWindow() {
        return getShowingWindow() != null;
    }

    /**
     * 显示指定的弹窗
     *
     * @param activity
     * @param manager
     * @param windowWrapper
     */
    private synchronized void show(Activity activity, FragmentManager manager, WindowWrapper windowWrapper) {
        if (!enable) {
            return;
        }
        if (windowWrapper != null && windowWrapper.getWindow() != null) {
            WindowWrapper topShowWindow = getShowingWindow();
            if (topShowWindow == null || !topShowWindow.isWindowShow()) {
                int priority = windowWrapper.getPriority();
                WindowWrapper maxPriorityWindow = getMaxPriorityWindow();
                if (maxPriorityWindow != null && priority >= maxPriorityWindow.getPriority() && windowWrapper.isCanShow()) {
                    if (windowWrapper.getWindow() != null && isActivityAlive(activity) && !windowWrapper.isWindowShow()) {
                        windowWrapper.setWindowShow(true);
                        windowWrapper.getWindow().setOnWindowDismissListener(() -> {
                            if (windowWrapper.getWindow().getClassName().equalsIgnoreCase(windowWrapper.getWindowName())) {
                                windowWrapper.setWindowShow(false);
                                mWindows.remove(windowWrapper.getWindowName());
                                if (windowWrapper.isAutoShowNext()) {
                                    showNext(activity, manager);
                                }
                            }
                        });
                        windowWrapper.getWindow().show(activity, manager);
                    }
                }
            }
        }
    }

    private boolean isActivityAlive(Activity activity) {
        if (activity == null
                || activity.isDestroyed()
                || activity.isFinishing()) {
            return false;
        }
        return true;
    }

    /**
     * 展示下一个优先级最大的Window
     */
    private synchronized void showNext(Activity activity, FragmentManager manager) {
        if (!enable) {
            return;
        }
        WindowWrapper windowWrapper = getMaxPriorityWindow();
        if (windowWrapper != null && !windowWrapper.isWindowShow() && windowWrapper.isCanShow()) {
            if (windowWrapper.getWindow() != null) {
                windowWrapper.setWindowShow(true);
                windowWrapper.getWindow().setOnWindowDismissListener(() -> {
                    if (windowWrapper.getWindow().getClassName().equalsIgnoreCase(windowWrapper.getWindowName())) {
                        windowWrapper.setWindowShow(false);
                        mWindows.remove(windowWrapper.getWindowName());
                        if (windowWrapper.isAutoShowNext()) {
                            showNext(activity, manager);
                        }
                    }
                });
                windowWrapper.getWindow().show(activity, manager);
            }
        }
    }

    /**
     * 获取当前栈中优先级最高的Window（优先级相同则返回后添加的弹窗）
     */
    private synchronized WindowWrapper getMaxPriorityWindow() {
        if (mWindows != null) {
            int maxPriority = -1;
            WindowWrapper targetWrapper = null;

            int i = 0;
            for (HashMap.Entry<String, WindowWrapper> entry : mWindows.entrySet()) {
                WindowWrapper windowWrapper = entry.getValue();
                if (i == 0) {
                    targetWrapper = windowWrapper;
                    maxPriority = windowWrapper.getPriority();
                } else {
                    if (windowWrapper.getPriority() >= maxPriority) {
                        targetWrapper = windowWrapper;
                        maxPriority = windowWrapper.getPriority();
                    }
                }
                i++;
            }

            if (targetWrapper != null) {
                return mWindows.get(targetWrapper.getWindowName());
            } else {
                return null;
            }
        }
        return null;
    }

    private synchronized WindowWrapper getTargetWindow(String windowName) {
        if (mWindows != null) {
            for (HashMap.Entry<String, WindowWrapper> entry : mWindows.entrySet()) {
                WindowWrapper windowWrapper = entry.getValue();
                if (windowWrapper != null && windowWrapper.getWindowName().equals(windowName)) {
                    return windowWrapper;
                }
            }
        }
        return null;
    }

    /**
     * 获取当前处于show状态的弹窗
     */
    private synchronized WindowWrapper getShowingWindow() {
        if (mWindows != null) {
            for (HashMap.Entry<String, WindowWrapper> entry : mWindows.entrySet()) {
                WindowWrapper windowWrapper = entry.getValue();
                if (windowWrapper != null && windowWrapper.getWindow() != null
                        && (windowWrapper.isWindowShow() || windowWrapper.getWindow().isShowing())) {
                    return windowWrapper;
                }
            }
        }
        return null;
    }

    private synchronized boolean hasAddWindow(WindowWrapper windowWrapper) {
        if (mWindows != null) {
            for (HashMap.Entry<String, WindowWrapper> entry : mWindows.entrySet()) {
                WindowWrapper wrapper = entry.getValue();
                if (windowWrapper != null) {
                    if (wrapper.getWindowName().equalsIgnoreCase(windowWrapper.getWindowName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setEnableWindow(boolean enable) {
        this.enable = enable;
    }

}