package com.ext.priority.window.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import com.ext.priority.window.WindowTaskManager;
import com.ext.priority.window.WindowWrapper;
import com.ext.priority.window.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initDialog();

        new Handler().postDelayed(() -> WindowTaskManager.getInstance().show(TestActivity.this), 3000);
    }

    private void initDialog() {
        int[] priority = new int[]{3, 1, 2};

        DemoDialog demoDialog = new DemoDialog(this);
        demoDialog.setTitle("温馨提示");
        demoDialog.setMessage("第一个弹窗,优先级：" + priority[0]);
        demoDialog.setCancelable(false);
        demoDialog.setButton(DialogInterface.BUTTON_POSITIVE, "关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              /*调用此方法传false告诉DialogManager此窗口
                dismiss是用户自己关闭的，而非被优先级更高的弹
                窗show后被挤出，这种情况优先级更高的弹窗dismiss
                后DialogManager不会重新show此弹窗*/
                demoDialog.dismiss();
            }
        });
        DemoPopupWindow popupWindow = new DemoPopupWindow(this);
        DemoActivity demoActivity = new DemoActivity();

        WindowTaskManager.getInstance().add(this, new WindowWrapper.Builder().window(demoDialog).priority(priority[0]).build());
        WindowTaskManager.getInstance().add(this, new WindowWrapper.Builder().window(popupWindow).priority(priority[1]).build());
        WindowTaskManager.getInstance().add(this, new WindowWrapper.Builder().window(demoActivity).priority(priority[2]).build());
    }

}