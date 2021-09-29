package com.ext.priority.window.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.ext.priority.window.IWindow;
import com.ext.priority.window.R;
import com.ext.priority.window.WindowTaskManager;
import com.ext.priority.window.WindowType;
import com.ext.priority.window.WindowWrapper;

public class TestActivity extends AppCompatActivity {

    /**
     * 活动弹窗的优先级
     */
    public final static int AD_PRIORITY = 1;
    /**
     * 更新弹窗的优先级
     */
    public final static int UPDATE_PRIORITY = 2;
    /**
     * 评分弹窗的优先级
     */
    public final static int ALERT_PRIORITY = 3;
    /**
     * other弹窗的优先级
     */
    public final static int OTHER_PRIORITY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        init();

        // 模拟网络延迟
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WindowTaskManager.getInstance().continueShow(TestActivity.this, getSupportFragmentManager());
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WindowTaskManager.getInstance().continueShow(TestActivity.this, getSupportFragmentManager());
            }
        }, 4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IWindow demoDialog = getDialogWindow();
                WindowWrapper windowWrapper = new WindowWrapper.Builder()
                        .priority(AD_PRIORITY)
                        .windowType(WindowType.DIALOG)
                        .window(getDialogWindow())
                        .setCanShow(true)
                        .setWindowName(demoDialog.getClassName())
                        .build();
                WindowTaskManager.getInstance().showWindow(TestActivity.this, getSupportFragmentManager(), windowWrapper);
            }
        }, 6000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WindowTaskManager.getInstance().clear();
    }

    /**
     * 统一按照优先级顺序初始化对话框
     */
    private void init() {
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
    }

    private IWindow getDialogWindow() {
        // Dialog
        DemoDialog demoDialog = new DemoDialog(this);
        demoDialog.setTitle("对话框");
        demoDialog.setMessage("Dialog Window");
        demoDialog.setCancelable(false);
        demoDialog.setButton(DialogInterface.BUTTON_POSITIVE, "关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                demoDialog.dismiss();
            }
        });
        return demoDialog;
    }

    private IWindow getPopupWindow() {
        // PopupWindow
        DemoPopupWindow popupWindow = new DemoPopupWindow(this);
        return popupWindow;
    }

    private IWindow getActivityWindow() {
        // Activity
        DemoActivity demoActivity = new DemoActivity();
        return demoActivity;
    }

}