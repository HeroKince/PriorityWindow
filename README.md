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
```

2.等待逻辑处理后显示

```
先初始化

WindowTaskManager.getInstance().addWindow(this, new WindowWrapper.Builder()
.priority(ALERT_PRIORITY)
.windowType(WindowType.ACTIVITY)
.setCanShow(false)
.build());


WindowTaskManager.getInstance().enableWindow(TestActivity.this, ALERT_PRIORITY, getActivityWindow());

```

WindowTaskManager.getInstance().disableWindow(TestActivity.UPDATE_PRIORITY);

  WindowTaskManager.getInstance().setBlockTask(false/true);



