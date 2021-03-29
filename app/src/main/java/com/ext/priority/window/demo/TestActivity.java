package com.ext.priority.window.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import com.ext.priority.window.WindowTaskManager;
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
     * alert弹窗的优先级
     */
    public final static int ALERT_PRIORITY = 3;
    /**
     * 登录弹窗的优先级
     */
    public final static int LOGIN_PRIORITY = 4;
    /**
     * other弹窗的优先级
     */
    public final static int OTHER_PRIORITY = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initDialog();

        new Handler().postDelayed(() -> WindowTaskManager.getInstance().show(TestActivity.this), 3000);
    }

    private void initDialog() {
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

        // PopupWindow
        DemoPopupWindow popupWindow = new DemoPopupWindow(this);

        // Activity
        DemoActivity demoActivity = new DemoActivity();

        WindowTaskManager.getInstance().add(this, new WindowWrapper.Builder().window(demoDialog).priority(AD_PRIORITY).setCanShow(true).build());
        WindowTaskManager.getInstance().add(this, new WindowWrapper.Builder().window(popupWindow).priority(UPDATE_PRIORITY).setCanShow(true).build());
        WindowTaskManager.getInstance().add(this, new WindowWrapper.Builder().window(demoActivity).priority(ALERT_PRIORITY).setCanShow(true).build());
    }

}