# PriorityWindow

按优先级顺序显示弹窗

### 使用方法

#### 1.直接显示

弹窗集成IWindow，并实现相关方法：

```
public class DemoDialog extends AlertDialog implements IWindow {
   ...
}

DemoActivity extends Activity implements IWindow{
   ...
}

public class DemoPopupWindow extends PopupWindow implements IWindow {
   ...
}

...
```

然后创建弹窗任务，最后调用showWindow方法显示即可：

```
IWindow demoDialog = getDialogWindow();
WindowWrapper windowWrapper = new WindowWrapper.Builder()
        .priority(AD_PRIORITY)
        .windowType(WindowType.DIALOG)
        .window(getDialogWindow())
        .setCanShow(true)
        .setWindowName(demoDialog.getClassName())
        .build();
WindowTaskManager.getInstance().showWindow(TestActivity.this, getSupportFragmentManager(), windowWrapper);
```

注意setWindowName要与IWindow中的getClassName()一致。

#### 2.延时等待显示

弹窗集成IWindow，并实现相关方法：

```
public class DemoDialog extends AlertDialog implements IWindow {
   ...
}

DemoActivity extends Activity implements IWindow{
   ...
}

public class DemoPopupWindow extends PopupWindow implements IWindow {
   ...
}

...
```

然后初始化弹窗任务：

```
IWindow activityWindow = getActivityWindow();
WindowTaskManager.getInstance().addWindow(new WindowWrapper.Builder()
        .priority(ALERT_PRIORITY)
        .windowType(WindowType.ACTIVITY)
        .window(activityWindow)
        .setWindowName(activityWindow.getClassName())
        .setCanShow(true)
        .build());

IWindow popupWindow = getPopupWindow();
WindowTaskManager.getInstance().addWindow(new WindowWrapper.Builder()
        .priority(UPDATE_PRIORITY)
        .windowType(WindowType.POUPOWINDOW)
        .window(getPopupWindow())
        .setWindowName(popupWindow.getClassName())
        .setCanShow(true)
        .build());
```

在要显示弹窗的地方调用：

```
 WindowTaskManager.getInstance().continueShow(context, getSupportFragmentManager());
```

如果不需要显示弹窗了，则调用disableWindow方法，将弹窗从队列中删除掉：

```
WindowTaskManager.getInstance().disableWindow(windowName);
```

#### 3.阻塞弹窗

有时候需要阻塞弹窗的显示，比如引导开启系统权限后，会显示系统的权限授权界面，等授权结束后再继续显示其他弹窗。

```
WindowTaskManager.getInstance().setEnableWindow(false/true);
```

#### 4.清除弹窗

```
WindowTaskManager.getInstance().clear();
```
退出应用的时候将任务栈清空。

#### 5.属性介绍

```
/**
 * 需要显示的窗口要继承这个接口
 */
private IWindow mWindow;

/**
 * 显示优先级，值越大优先级越高，窗口优先显示
 */
private int mPriority;

/**
 * 弹窗类型，可以是Activity、Dialog、PopubWindow等
 */
private WindowType mWindowType;

/**
 * 是否满足show的条件，默认是TRUE立即显示，如果需要延时显示设置FALSE
 */
private boolean isCanShow;

/**
 * 弹窗名称，需要与IWindow中的getClassName()一致
 */
private String mWindowName;

/**
 * 当前弹窗关闭后，是否自动显示下一个，默认是TRUE
 */
private boolean autoShowNext = true;

/**
 * 是否强制显示弹窗，比如可能存在两个Activity同时显示弹窗的情况
 */
private boolean forceShow;
```

### Jitpack引入：

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
		}
	}
   
dependencies {
	  implementation 'com.github.Geekince:PriorityWindow:Tag'
}
   
```

