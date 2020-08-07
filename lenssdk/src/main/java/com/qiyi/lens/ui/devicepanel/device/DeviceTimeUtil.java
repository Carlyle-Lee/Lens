package com.qiyi.lens.ui.devicepanel.device;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.qiyi.lens.utils.ApplicationLifecycle;

import org.qiyi.basecore.taskmanager.TM;
import org.qiyi.basecore.taskmanager.Task;

/**
 * https://www.cnblogs.com/zhchoutai/p/6734991.html
 */
public class DeviceTimeUtil {

    private final String TAG = "CPUTimeUtil";
    static final int EVENT_CPU = 1;
    static final int EVENT_MEMO = 2;
    private volatile boolean isPaused;

    /**
     * pause reading cpu time
     */
    public void pause() {
        isPaused = true;
        TM.cancelTaskByToken(this);
    }


    /**
     * resume reading cpu time
     */
    public void resume() {
        new Task() {
            @Override
            public void doTask() {
                handleDataReading();
                next();
            }
        }.setToken(this)
                .setThreadPriority(Thread.MIN_PRIORITY)
                .postSerial(TAG);

    }

    private void next() {
        TM.postAsyncDelay(new Runnable() {
            @Override
            public void run() {
                if (!isPaused) resume();
            }
        }, 100);

    }


    private void handleDataReading() {

        Context context = ApplicationLifecycle.getInstance().getContext();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);

        activityManager.getProcessMemoryInfo(new int[]{Process.myPid()});

        DeviceInfoReader reader = new DeviceInfoReader();
        CpuInfo cpuInfo = reader.read();
        if (cpuInfo == null) return;
        TM.triggerEvent(this, EVENT_CPU, cpuInfo);
        TM.triggerEvent(this, EVENT_MEMO, info);

    }

}
