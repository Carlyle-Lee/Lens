package com.qiyi.lens.ui.system;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiyi.lens.ui.FloatingPanel;
import com.qiyi.lens.ui.FullScreenPanel;
import com.qiyi.lens.ui.widget.drawable.GrowthDrawable;
import com.qiyi.lens.ui.widget.tableView.DefaultItemBinder;
import com.qiyi.lens.ui.widget.tableView.TableBuilder;
import com.qiyi.lens.ui.widget.tableView.TableView;
import com.qiyi.lens.utils.Utils;
import com.qiyi.lenssdk.R;

import org.qiyi.basecore.taskmanager.TickTask;

public class SystemInfoPanel extends FullScreenPanel {
    private TableBuilder mSysInfoBuilder;
    private TableBuilder mUsageBuilder;
    private TableBuilder mDetailBuilder;
    private TableView mSysInfoTableView;
    private TableView mUsageTableView;
    private TableView mAppDetailView;


    public SystemInfoPanel(FloatingPanel panel) {
        super(panel);
        setTitle(R.string.lens_title_memo_info);
    }

    @Override
    public View onCreateView(ViewGroup group) {
        return inflateView(R.layout.lens_system_info_panle, group);
    }

    @Override
    public void onViewCreated(View view) {
        super.onViewCreated(view);
        // build data
        mSysInfoTableView = view.findViewById(R.id.lens_sys_info_cpu);
        mAppDetailView = view.findViewById(R.id.lens_sys_info_memo);
        mUsageTableView = view.findViewById(R.id.lens_memo_sys_info);


        buildMemoData();

        new TickTask() {

            @Override
            public void onTick(int loopTime) {

                if (mMemoDataBuilder != null) {
                    mMemoDataBuilder.update();
                    String[] data = mMemoDataBuilder.buildUsageInfo();
                    mUsageBuilder.setData(data);
                    mUsageBuilder.notifyDataChange();

                    data = mMemoDataBuilder.buildSystemMemoInfo();
                    mSysInfoBuilder.setData(data);
                    mSysInfoBuilder.notifyDataChange();

                    data = mMemoDataBuilder.buildAppDetailInfo();
                    mDetailBuilder.setData(data);
                    mDetailBuilder.notifyDataChange();

                }

            }
        }.setIntervalWithFixedDelay(500)
                .postUI();


    }

    //    private TableBuilder mMemoBuilder;
    private MemoDataBuilder mMemoDataBuilder;

    private void buildAMSMemoInfo() {
        String[] data = mMemoDataBuilder.buildUsageInfo();
        mUsageBuilder = TableBuilder.obtain(context)
                .setTableView(mUsageTableView)
                .setColumnCountRowCount(2, 2)
                .setStrokeWidth(2, 4)
                .setStretchableColumns(0, 1, 2)
                .setColumnNames(mMemoDataBuilder.getUsageColumnNames())
                .setRowNames(mMemoDataBuilder.getUsageRowNames())
                .setDataBinder(new MemoInfoBinder(getActivity()))
                .setData(data);
        mUsageBuilder.build();
    }


    /**
     * 1，验证两种方式获取到的数据的差异
     * 2，验证relase 版本使用debug 方式是否也能获取到数据。*
     */
    private void buildMemoData() {

        mMemoDataBuilder = new MemoDataBuilder();
        mMemoDataBuilder.update();

        buildAMSMemoInfo();

        String[] data = mMemoDataBuilder.buildSystemMemoInfo();
        mSysInfoBuilder = TableBuilder.obtain(getActivity())
                .setColumnCountRowCount(2, 0)
                .setTableView(mSysInfoTableView)
                .setDataBinder(new MemoInfoBinder(getActivity()))
                .setStrokeWidth(2, 4)
                .setStretchableColumns(0, 1)
                .setData(data);
        mSysInfoBuilder.build();

        data = mMemoDataBuilder.buildAppDetailInfo();

        mDetailBuilder = TableBuilder.obtain(getActivity())
                .setTableView(mAppDetailView)
                .setData(data)
                .setStrokeWidth(2, 4)
                .setColumnCountRowCount(2, 2)
                .setStretchableColumns(0, 1)
                .setDataBinder(new MemoInfoBinder(getActivity()));
        mDetailBuilder.build();
    }

    class MemoInfoBinder extends DefaultItemBinder {

        public MemoInfoBinder(Context context) {
            super(context);
        }

        @Override
        public void bindData(String data, View view, int row, int column) {
            Drawable drawable = view.getBackground();
            if (data != null && drawable instanceof GrowthDrawable) {
                TextView textView = (TextView) view;
                int compare = Utils.storageDataCompare(data, textView.getText().toString());
                if (compare != 0) {
                    ((GrowthDrawable) drawable).startGrow(view.getWidth(), view.getHeight(), compare > 0);
                }
            }
            super.bindData(data, view, row, column);
        }

        @Override
        //0x56888888
        public View createItemView(ViewGroup parent, int row, int column) {
            View view = super.createItemView(parent, row, column);
            if (row >= 0 && column >= 0) {
                view.setBackgroundDrawable(new GrowthDrawable());
            }
            return view;
        }

    }

}
