package com.ext.priority.window.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ext.priority.window.IWindow;
import com.ext.priority.window.OnWindowDismissListener;
import com.ext.priority.window.OnWindowShowListener;
import com.ext.priority.window.R;
import com.ext.priority.window.WindowHelper;

public class DemoActivity extends AppCompatActivity implements IWindow {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    @Override
    public void show(Activity activity) {
        activity.startActivity(new Intent(activity, DemoActivity.class));
        WindowHelper.getInstance().getActivityShowListener().onShow();
    }

    @Override
    protected void onDestroy() {
        WindowHelper.getInstance().getActivityDismissListener().onDismiss();
        super.onDestroy();
    }

    @Override
    public void dismiss() {
        finish();
    }

    @Override
    public void setOnWindowDismissListener(OnWindowDismissListener listener) {
        WindowHelper.getInstance().setActivityDismissListener(listener);
    }

    @Override
    public void setOnWindowShowListener(OnWindowShowListener listener) {
        WindowHelper.getInstance().setActivityShowListener(listener);
    }

    @Override
    public boolean isCanShow() {
        return true;
    }

}
