# PriorityWindow

按优先级顺序显示弹窗

### 使用方法

初始化需要按照顺序显示的弹窗

1.直接显示

```
WindowTaskManager.getInstance().addWindow(this, new WindowWrapper.Builder()
.priority(ALERT_PRIORITY)
.windowType(WindowType.ACTIVITY)
.setCanShow(false)
.window(new Dialog())
.build());

WindowTaskManager.getInstance().show(activity);

```

2.等待逻辑处理后显示

先初始化：

```
WindowTaskManager.getInstance().addWindow(this, new WindowWrapper.Builder()
.priority(ALERT_PRIORITY)
.windowType(WindowType.ACTIVITY)
.setCanShow(false)
.build());
```

在要显示的地方调用：

```
WindowTaskManager.getInstance().enableWindow(TestActivity.this, ALERT_PRIORITY, getActivityWindow());
```

如果不需要显示了，则调用：

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


