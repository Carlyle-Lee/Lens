package com.qiyi.lens.ui.system;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Process;
import android.util.Log;

import com.qiyi.lens.utils.ApplicationLifecycle;
import com.qiyi.lens.utils.Utils;

public class MemoDataBuilder {

    private ActivityManager mActivityManager;
    //in B
    private long freeMemo;
    private long maxMemo;
    private long totalMemo;


    private long mTotalSys;
    private long mFreeSys;
    private long mThreshHold;
    private boolean mLMK;

    private String mMemoryClassString;

    private Debug.MemoryInfo mAppDetailMemoInfo;

    public MemoDataBuilder() {
        Context context = ApplicationLifecycle.getInstance().getContext();
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = mActivityManager.getMemoryClass();
        mMemoryClassString = Utils.parseStorageSize(memoryClass, 2);

    }


    public void update() {

        mAppDetailMemoInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(mAppDetailMemoInfo);

        // bytes
        freeMemo = Runtime.getRuntime().freeMemory();
        maxMemo = Runtime.getRuntime().maxMemory();
        totalMemo = Runtime.getRuntime().totalMemory();

        ActivityManager.MemoryInfo amsMemoInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(amsMemoInfo);
        mFreeSys = amsMemoInfo.availMem;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTotalSys = amsMemoInfo.totalMem;
        }
        mThreshHold = amsMemoInfo.threshold;
        mLMK = amsMemoInfo.lowMemory;


    }

    public String getFreeMemo() {
        return Utils.parseStorageSize(freeMemo, 0);
    }

    public String getTotalMemo() {
        return Utils.parseStorageSize(totalMemo, 0);
    }

    public String[] buildUsageInfo() {
        return new String[]{
                getTotalMemo(),
                getFreeMemo(),
                Utils.parseStorageSize(mTotalSys, 0),
                Utils.parseStorageSize(mFreeSys, 0)
        };
    }

    public String[] getUsageRowNames() {
        return new String[]{
                "App",
                "System"
        };
    }

    public String[] getUsageColumnNames() {
        return new String[]{
                "Total",
                "Free"
        };
    }


    public String[] buildSystemMemoInfo() {
        return new String[]{
                "PID", Process.myPid() + "",
                "Java (P)", getTotalMemo(),
                "Native (P)", Utils.parseStorageSize(Debug.getNativeHeapSize(), 0),
                "Max Java Heap", mMemoryClassString,
                "ThreshHold(Sys)", Utils.parseStorageSize(mThreshHold, 0),
                "LMK(Sys)", mLMK + ""
        };
    }

    public String[] buildAppDetailInfo() {

        Log.d("TSTSTSTS", "s: " + mAppDetailMemoInfo.getTotalPss());
        return new String[]{
                "dalvikPrivateDirty", Utils.parseStorageSize(mAppDetailMemoInfo.dalvikPrivateDirty, 1),
                "nativePrivateDirty", Utils.parseStorageSize(mAppDetailMemoInfo.nativePrivateDirty, 1),
                "otherPrivateDirty", Utils.parseStorageSize(mAppDetailMemoInfo.otherPrivateDirty, 1),
                "dalvikSharedDirty", Utils.parseStorageSize(mAppDetailMemoInfo.dalvikSharedDirty, 1),
                "nativeSharedDirty", Utils.parseStorageSize(mAppDetailMemoInfo.nativeSharedDirty, 1),
                "otherSharedDirty", Utils.parseStorageSize(mAppDetailMemoInfo.otherSharedDirty, 1),
                "dalvikPss", Utils.parseStorageSize(mAppDetailMemoInfo.dalvikPss, 1),
                "nativePss", Utils.parseStorageSize(mAppDetailMemoInfo.nativePss, 1),
                "otherPss", Utils.parseStorageSize(mAppDetailMemoInfo.otherPss, 1),
                "TotalPss", Utils.parseStorageSize(mAppDetailMemoInfo.getTotalPss(), 1)

        };

    }
}
