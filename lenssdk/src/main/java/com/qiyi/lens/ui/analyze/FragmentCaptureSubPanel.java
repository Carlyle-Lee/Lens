package com.qiyi.lens.ui.analyze;

import android.view.ViewGroup;

import com.qiyi.lens.ui.abtest.KeyValueSubPanelView;
import com.qiyi.lens.utils.reflect.FragmentInfo;

/**
 * 模块： 页面分析
 * 功能： 在页面分析中，对抓取到的 Fragment 弹出 sub panel；并提供展示
 * 「Fragment 对应的View」 的功能入口。
 */
public class FragmentCaptureSubPanel extends KeyValueSubPanelView {

    private FragmentInfo mFragmentInfo;

    public FragmentCaptureSubPanel(ViewGroup root) {
        super(root);
    }


    public void setFragmentInfo(FragmentInfo fragmentInfo) {
        mFragmentInfo = fragmentInfo;
    }
}

