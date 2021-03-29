package com.ext.priority.window.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import com.ext.priority.window.IWindow;
import com.ext.priority.window.WindowTaskManager;
import com.ext.priority.window.WindowType;
import com.ext.priority.window.WindowWrapper;
import com.ext.priority.window.R;

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

        initDialog();

        // 模拟网络延迟
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WindowTaskManager.getInstance().enableWindow(TestActivity.this, ALERT_PRIORITY, getActivityWindow());
            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WindowTaskManager.getInstance().enableWindow(TestActivity.this, UPDATE_PRIORITY, getPopupWindow());
            }
        }, 3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WindowTaskManager.getInstance().enableWindow(TestActivity.this, AD_PRIORITY, getDialogWindow());
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WindowTaskManager.getInstance().clear();
    }

    /**
     * 统一按照优先级顺序初始化对话框
     */
    private void initDialog() {
        WindowTaskManager.getInstance().addWindow(this, new WindowWrapper.Builder().priority(ALERT_PRIORITY).windowType(WindowType.ACTIVITY).setCanShow(false).build());
        WindowTaskManager.getInstance().addWindow(this, new WindowWrapper.Builder().priority(UPDATE_PRIORITY).windowType(WindowType.POUPOWINDOW).setCanShow(false).build());
        WindowTaskManager.getInstance().addWindow(this, new WindowWrapper.Builder().priority(AD_PRIORITY).windowType(WindowType.DIALOG).setCanShow(false).build());
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