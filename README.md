# PriorityWindow

按优先级顺序显示弹窗

### 使用方法

1.直接显示

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

2.等待逻辑处理后显示

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

如果不需要显示弹窗了，则调用：

```
WindowTaskManager.getInstance().disableWindow(windowName);
```

3.阻塞弹窗

有时候需要阻塞弹窗的显示，比如引导开启系统权限后，会显示系统的权限授权界面，等授权结束后再继续显示其他弹窗。

```
WindowTaskManager.getInstance().setEnableWindow(false/true);
```

4.清除弹窗

```
WindowTaskManager.getInstance().clear();
```
退出应用的时候将任务栈清空。

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

