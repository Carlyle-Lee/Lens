/*
 *
 * Copyright (C) 2020 iQIYI (www.iqiyi.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.qiyi.lens.ui.devicepanel.device;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qiyi.lens.ui.FloatingPanel;
import com.qiyi.lens.ui.devicepanel.blockInfos.AbsBlockInfo;
import com.qiyi.lens.ui.system.SystemInfoPanel;
import com.qiyi.lens.ui.widget.tableView.TableBuilder;
import com.qiyi.lenssdk.R;

import org.qiyi.basecore.taskmanager.EventTask;

/**
 * 支持实时更新CPU 信息
 */
public class DeviceInfo extends AbsBlockInfo {
    private TableBuilder builder;
    private LinearLayout mContainerLl;
    DeviceTimeUtil mTimeUtil;
    private DeviceInfoEvent mDeviceInfoEvent;

    public DeviceInfo(FloatingPanel panel) {
        super(panel);
        mTimeUtil = new DeviceTimeUtil();
        mDeviceInfoEvent = new DeviceInfoEvent();
    }

    @Override
    public void bind(View textView) {
        mDeviceInfoEvent
                .registerGroupedEvents(mTimeUtil, DeviceTimeUtil.EVENT_CPU, DeviceTimeUtil.EVENT_MEMO)
                .postUI();
        mTimeUtil.resume();
    }

    public void unBind() {
        mDeviceInfoEvent.unregister();
        mTimeUtil.pause();
        builder = null;
    }


    @Override
    public View createView(ViewGroup parent) {
        View root = inflateView(parent, R.layout.lens_block_device_info);
        mContainerLl = root.findViewById(R.id.ll_container);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SystemInfoPanel(getPanel()).show();
            }
        });

        Context context = parent.getContext();
        DisplayMetrics df = context.getResources().getDisplayMetrics();
        String[] data = new String[]{
                "CPU", "Memo", "SDK", "--", "--", Build.VERSION.SDK_INT + "",
                "分辨率", "像素密度", "", df.widthPixels + "\n" + df.heightPixels, df.density + "", ""
        };
        builder = TableBuilder.obtain(context);
        View view = builder
                .setData(data)
                .setItemTextSize(12)
                .setStrokeWidth(3, 5)
                .setStretchableColumns(0, 1)
                .setColumnCountRowCount(3, 4)
                .build();

        LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(-2, -2);
        clp.gravity = Gravity.CENTER_HORIZONTAL;
        view.setLayoutParams(clp);
        mContainerLl.addView(view);
        return root;
    }

    private String parseMemo(long memoSize) {
        float var = memoSize;
        float K = 1024;
        int shift = 0;
        while (var > K && shift < 3) {
            var = var / K;
            shift++;
        }

        String ampunt = String.format("%.2f", var);
        switch (shift) {
            case 0:
                return ampunt + "B";
            case 1:
                return ampunt + "K";
            case 2:
                return ampunt + "M";
            case 3:
                return ampunt + "G";
        }
        return ampunt;
    }

    private void setMemoInfo(ActivityManager.MemoryInfo memoInfo) {
        if (builder != null) {
            int var = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                var = (int) (100 * memoInfo.availMem * 1f / memoInfo.totalMem);
                builder.setItemData(var + "%:" + parseMemo(memoInfo.totalMem), 1, 1);
            }
        }
    }

    private void setCpuInfo(CpuInfo cpuInfo) {
        if (builder != null) {
            builder.setItemData(cpuInfo.getCpuRate(), 1, 0);
        }
    }


    class DeviceInfoEvent extends EventTask {

        @Override
        public void onEvent(int eventId, Object msg) {
            if (builder == null) return;
            if (eventId == DeviceTimeUtil.EVENT_CPU) {
                setCpuInfo((CpuInfo) msg);
            } else if (eventId == DeviceTimeUtil.EVENT_MEMO) {
                setMemoInfo((ActivityManager.MemoryInfo) msg);
            }

        }
    }
}
