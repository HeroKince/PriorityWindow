package com.ext.priority.window;

/**
 * 窗口参数类
 */
public class WindowWrapper {

    /**
     * 窗口
     */
    private IWindow mWindow;

    /**
     * 优先级，值越大优先级越高
     */
    private int mPriority;

    /**
     * 当前是否处于show状态
     */
    private boolean isShowing;

    /**
     * 是否满足show的条件
     */
    private boolean isCanShow;

    //弹窗类型
    private WindowType mWindowType;

    private WindowWrapper(Builder builder) {
        mWindow = builder.window;
        mPriority = builder.priority;
        mWindowType = builder.windowType;
        isCanShow = builder.isCanShow;
    }

    public IWindow getWindow() {
        return mWindow;
    }

    public void setWindow(IWindow window) {
        this.mWindow = window;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        this.mPriority = priority;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    public WindowType getWindowType() {
        return mWindowType;
    }

    public void setWindowType(WindowType mWindowType) {
        this.mWindowType = mWindowType;
    }

    public boolean isCanShow() {
        return isCanShow;
    }

    public void setCanShow(boolean canShow) {
        isCanShow = canShow;
    }

    public static class Builder {

        /**
         * 窗口
         */
        private IWindow window;

        /**
         * 优先级，值越大优先级越高
         */
        private int priority;

        //弹窗类型
        private WindowType windowType;

        /**
         * 是否满足show的条件
         */
        private boolean isCanShow;

        public Builder window(IWindow window) {
            this.window = window;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder windowType(WindowType type) {
            this.windowType = type;
            return this;
        }

        public Builder setCanShow(boolean canShow) {
            isCanShow = canShow;
            return this;
        }

        public WindowWrapper build() {
            return new WindowWrapper(this);
        }
    }

}