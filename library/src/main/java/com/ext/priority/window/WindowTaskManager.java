package com.ext.priority.window;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

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

    private List<WindowWrapper> mWindows;
    private boolean isBlockTask;// 是否阻塞所有弹窗显示

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
     * @param windowWrapper 待显示的弹窗
     */
    public synchronized void addWindow(Activity activity, WindowWrapper windowWrapper) {
        if (windowWrapper != null) {
            if (mWindows == null) {
                mWindows = new ArrayList<>();
            }

            if (windowWrapper.getWindow() != null) {
                windowWrapper.getWindow().setOnWindowDismissListener(new OnWindowDismissListener() {
                    @Override
                    public void onDismiss() {
                        mWindows.remove(windowWrapper);
                        showNext(activity);
                    }
                });
            }

            mWindows.add(windowWrapper);
        }
    }

    /**
     * 弹窗满足展示条件
     *
     * @param priority
     */
    public synchronized void enableWindow(Activity activity, int priority, IWindow window) {
        WindowWrapper windowWrapper = getTargetWindow(priority);
        if (windowWrapper != null) {
            if (windowWrapper.getWindow() == null) {
                window.setOnWindowDismissListener(new OnWindowDismissListener() {
                    @Override
                    public void onDismiss() {
                        mWindows.remove(windowWrapper);
                        showNext(activity);
                    }
                });
            }
            windowWrapper.setCanShow(true);
            windowWrapper.setWindow(window);
            if (!isBlockTask) {
                show(activity, windowWrapper);
            }
        } else {
            WindowWrapper newWindowWrapper = new WindowWrapper.Builder()
                    .priority(priority)
                    .setCanShow(true)
                    .window(window)
                    .build();
            window.setOnWindowDismissListener(new OnWindowDismissListener() {
                @Override
                public void onDismiss() {
                    mWindows.remove(newWindowWrapper);
                    showNext(activity);
                }
            });
            addWindow(activity, newWindowWrapper);
            if (!isBlockTask) {
                show(activity, newWindowWrapper);
            }
        }
    }

    /**
     * 移除不需要显示弹窗
     *
     * @param priority
     */
    public synchronized void disableWindow(int priority) {
        WindowWrapper windowWrapper = getTargetWindow(priority);
        if (windowWrapper != null && windowWrapper.getWindow() != null) {
            if (mWindows != null) {
                mWindows.remove(windowWrapper);
            }
        }
    }

    /**
     * 展示弹窗
     * 从优先级最高的Window开始显示
     */
    public synchronized void show(Activity activity) {
        WindowWrapper windowWrapper = getMaxPriorityWindow();
        if (windowWrapper != null && windowWrapper.isCanShow()) {
            IWindow window = windowWrapper.getWindow();
            if (window != null && isActivityAlive(activity)) {
                window.show(activity);
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
                for (int i = 0, size = mWindows.size(); i < size; i++) {
                    if (mWindows.get(i) != null) {
                        IWindow window = mWindows.get(i).getWindow();
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

    /**
     * 是否阻塞弹窗
     *
     * @return
     */
    public boolean isBlockTask() {
        return isBlockTask;
    }

    /**
     * 设置阻塞弹窗
     * 例如系统权限弹窗显示完再显示其他弹窗
     *
     * @param blockTask
     */
    public void setBlockTask(boolean blockTask) {
        isBlockTask = blockTask;
    }

    /**
     * 显示指定的弹窗
     *
     * @param windowWrapper
     */
    private synchronized void show(Activity activity, WindowWrapper windowWrapper) {
        if (windowWrapper != null && windowWrapper.getWindow() != null) {
            WindowWrapper topShowWindow = getShowingWindow();
            if (topShowWindow == null) {
                int priority = windowWrapper.getPriority();
                WindowWrapper maxPriorityWindow = getMaxPriorityWindow();
                if (maxPriorityWindow != null && windowWrapper.isCanShow() && priority >= maxPriorityWindow.getPriority()) {
                    if (windowWrapper.getWindow() != null && isActivityAlive(activity)) {
                        windowWrapper.getWindow().show(activity);
                    }
                }
            }
        }
    }
    /**/

    /**
     * 展示下一个优先级最大的Window
     */
    private synchronized void showNext(Activity activity) {
        if (isBlockTask) {
            return;
        }

        WindowWrapper windowWrapper = getMaxPriorityWindow();
        if (windowWrapper != null && windowWrapper.isCanShow()) {
            if (windowWrapper.getWindow() != null && isActivityAlive(activity)) {
                windowWrapper.getWindow().show(activity);
            }
        }
    }

    private boolean isActivityAlive(Activity activity) {
        return activity != null
                && !activity.isDestroyed()
                && !activity.isFinishing();
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

    private synchronized WindowWrapper getTargetWindow(int priority) {
        if (mWindows != null) {
            for (int i = 0, size = mWindows.size(); i < size; i++) {
                WindowWrapper windowWrapper = mWindows.get(i);
                if (windowWrapper != null && windowWrapper.getPriority() == priority) {
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
            for (int i = 0, size = mWindows.size(); i < size; i++) {
                WindowWrapper windowWrapper = mWindows.get(i);
                if (windowWrapper != null && windowWrapper.getWindow() != null && windowWrapper.getWindow().isShowing()) {
                    return windowWrapper;
                }
            }
        }
        return null;
    }

}