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
     * 弹窗类型
     */
    private WindowType mWindowType;

    /**
     * 是否满足show的条件
     */
    private boolean isCanShow;

    /**
     * 弹窗名称
     */
    private String mWindowName;

    /**
     * 当前弹窗关闭后，是否自动显示下一个
     */
    private boolean autoShowNext = true;

    /**
     * Dialog的show方法不及时，此处作为备选
     */
    private boolean isWindowShow;

    private WindowWrapper(Builder builder) {
        mWindow = builder.window;
        mPriority = builder.priority;
        mWindowType = builder.windowType;
        mWindowName = builder.windowName;
        autoShowNext = builder.autoShowNext;
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

    public WindowType getWindowType() {
        return mWindowType;
    }

    public void setWindowType(WindowType mWindowType) {
        this.mWindowType = mWindowType;
    }

    public String getWindowName() {
        return mWindowName;
    }

    public void setWindowName(String mWindowName) {
        this.mWindowName = mWindowName;
    }

    public boolean isAutoShowNext() {
        return autoShowNext;
    }

    public void setAutoShowNext(boolean autoShowNext) {
        this.autoShowNext = autoShowNext;
    }

    public boolean isWindowShow() {
        return isWindowShow;
    }

    public void setWindowShow(boolean windowShow) {
        isWindowShow = windowShow;
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

        /**
         * 弹窗类型
         */
        private WindowType windowType;

        /**
         * 是否满足show的条件
         */
        private boolean isCanShow;

        /**
         * 弹窗名称
         */
        private String windowName;

        /**
         * 当前弹窗关闭后，是否自动显示下一个
         */
        private boolean autoShowNext = true;

        /**
         * Dialog的show方法不及时，此处作为备选
         */
        private boolean isWindowShow;

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

        public Builder setWindowName(String windowName) {
            this.windowName = windowName;
            return this;
        }

        public Builder setAutoShowNext(boolean show) {
            autoShowNext = show;
            return this;
        }

        public Builder setWindowShow(boolean show) {
            isWindowShow = show;
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

    @Override
    public String toString() {
        return "WindowWrapper{" +
                "mWindowName='" + mWindowName + '\'' +
                ", isWindowShow=" + isWindowShow +
                '}';
    }

}