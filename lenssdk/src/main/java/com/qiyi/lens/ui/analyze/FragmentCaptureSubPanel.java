package com.qiyi.lens.ui.analyze;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.qiyi.lens.ui.abtest.SubPanelView;

/**
 * 模块： 页面分析
 * 功能： 在页面分析中，对抓取到的 Fragment 弹出 sub panel；并提供展示
 * 「Fragment 对应的View」 的功能入口。
 */
public class FragmentCaptureSubPanel extends SubPanelView {

    public FragmentCaptureSubPanel(ViewGroup root) {
        super(root);
    }


    public void add(Fragment fragment){

    }

    public void remove(Fragment fragment) {

    }
}

