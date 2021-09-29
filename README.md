# PriorityWindow

按优先级顺序显示弹窗

### 使用方法

1.直接显示

弹窗集成IWindow，并实现相关方法：

```
public class DemoDialog extends AlertDialog implements IWindow {
   ...
}
```

然后将弹窗添加到任务队列中去：

```
WindowTaskManager.getInstance().addWindow(this, new WindowWrapper.Builder()
.priority(ALERT_PRIORITY)
.windowType(WindowType.ACTIVITY)
.setCanShow(false)
.window(new Dialog())
.build());

```

最后调用显示弹窗方法：

```
WindowTaskManager.getInstance().show(activity);
```

2.等待逻辑处理后显示

弹窗集成IWindow，并实现相关方法：

```
public class DemoDialog extends AlertDialog implements IWindow {
   ...
}
```

然后初始化弹窗任务：

```
WindowTaskManager.getInstance().addWindow(this, new WindowWrapper.Builder()
.priority(ALERT_PRIORITY)
.windowType(WindowType.ACTIVITY)
.setCanShow(false)
.build());
```

在要显示弹窗的地方调用：

```
WindowTaskManager.getInstance().enableWindow(TestActivity.this, ALERT_PRIORITY, getActivityWindow());
```

如果不需要显示弹窗了，则调用：

```
WindowTaskManager.getInstance().disableWindow(TestActivity.ALERT_PRIORITY);
```

3.阻塞弹窗

有时候需要阻塞弹窗的显示，比如引导开启系统权限后，会显示系统的权限授权界面，等授权结束后再继续显示其他弹窗。

```
WindowTaskManager.getInstance().setBlockTask(false/true);
WindowTaskManager.getInstance().show(activity);
```

4.清除弹窗

```
WindowTaskManager.getInstance().clear();
```
退出应用的时候将任务栈清空。

Jitpack引入：

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

