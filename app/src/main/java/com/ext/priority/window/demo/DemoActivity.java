package com.ext.priority.window.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.ext.priority.window.IWindow;
import com.ext.priority.window.OnWindowDismissListener;
import com.ext.priority.window.R;
import com.ext.priority.window.WindowHelper;

public class DemoActivity extends Activity implements IWindow {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowHelper.getInstance().setActivityShow(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        WindowHelper.getInstance().setActivityShow(false);
    }

    @Override
    protected void onDestroy() {
        WindowHelper.getInstance().getActivityDismissListener().onDismiss();
        super.onDestroy();
    }

    @Override
    public String getClassName() {
        return DemoActivity.class.getSimpleName();
    }

    @Override
    public void show(Activity activity, FragmentManager manager) {
        activity.startActivity(new Intent(activity, DemoActivity.class));
        //WindowTaskManager.getInstance().disableWindow(TestActivity.UPDATE_PRIORITY);
    }

    @Override
    public void dismiss() {
        finish();
    }

    @Override
    public boolean isShowing() {
        return WindowHelper.getInstance().isActivityShow();
    }

    @Override
    public void setOnWindowDismissListener(OnWindowDismissListener listener) {
        WindowHelper.getInstance().setActivityDismissListener(listener);
    }

}
