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
package com.qiyi.lens.ui.widget;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.CallSuper;

import com.qiyi.lens.ui.abtest.content.ValueContent;
import com.qiyi.lenssdk.R;


/**
 * 底部面板
 */
public abstract class SubPanelView<T> implements View.OnClickListener {
    private ViewGroup viewRoot;
    private ViewGroup viewContainer;
    private View closeBtn;
    private int dur = 100;
    private ValueContent valueContent;
    private String _key;
    private DismissCallback _callback;

    public SubPanelView(ViewGroup root) {
        //[必须是viewGroup ]
        viewRoot = (ViewGroup) root;
        viewContainer = root.findViewById(R.id.lens_sub_panel_container);
        closeBtn = root.findViewById(R.id.lens_sub_panel_close_btn);
        closeBtn.setOnClickListener(this);
    }


    //[attach content view into parent]
    @CallSuper
    public void showData(T data) {
        if (data != null) {
            //[do show]
            loadContentView(data);
            if (viewRoot.getVisibility() != View.VISIBLE) {
                viewRoot.setVisibility(View.VISIBLE);
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0
                        , Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
                translateAnimation.setDuration(dur + 50);
                translateAnimation.setInterpolator(new AccelerateInterpolator());
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                viewRoot.startAnimation(translateAnimation);
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.lens_sub_panel_close_btn) {
            dismiss();
        }

    }

    public void dismiss() {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
        translateAnimation.setDuration(dur);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewRoot.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        viewRoot.startAnimation(translateAnimation);

        if (valueContent != null) {
            valueContent.detachView();
            valueContent = null;
        }

        if (_callback != null) {
            _callback.onDismiss();
        }
    }

    //[viewContainer as parent to load view]
    protected abstract void loadContentView(T value);


    public void setOnDismissCallback(DismissCallback call) {
        _callback = call;
    }

    public interface DismissCallback {
        void onDismiss();
    }

    public ViewGroup getPanelRoot(){
        return viewContainer;
    }


}
