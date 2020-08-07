package com.qiyi.lens.ui.widget.tableView;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiyi.lens.utils.StringUtil;

/**
 *
 */
public class DefaultItemBinder implements TableBuilder.ItemDataBinder {
    private int mItemPadding = 6;
    private int mTextSize;
    private int mTextColor = Color.BLACK;
    private Context mContext;
    private int mNameTextSize;
    private int mNameTextColor;

    public DefaultItemBinder(Context context) {
        this.mContext = context;
    }

    public DefaultItemBinder setItemPadding(int padding) {
        mItemPadding = padding;
        return this;
    }


    public DefaultItemBinder setTextInfo(int textSize, int textColor) {

        mTextSize = textSize;
        mTextColor = textColor;
        return this;
    }

    public DefaultItemBinder setNamesTextInfo(int textSize, int textColor) {
        mNameTextSize = textSize;
        mNameTextColor = textColor;
        return this;
    }


    /**
     * if colum is -1 : bind row name
     *
     * @param content
     * @param view
     * @param row
     * @param column
     */
    @Override
    public void bindData(String content, View view, int row, int column) {
        TextView textView = (TextView) view;
        if (!StringUtil.isNullOrEmpty(content)) textView.setText(content);
    }

    @Override
    public View createItemView(ViewGroup parent, int row, int column) {
        if (row < 0 || column < 0) {
            return createDefaultItemView(mContext, mNameTextSize, mNameTextColor);
        } else {
            return createDefaultItemView(mContext, mTextSize, mTextColor);
        }

    }

    private TextView createDefaultItemView(Context context, int textSize, int textColor) {
        TextView textView = new TextView(context);
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(mItemPadding, 0, mItemPadding, 0);
        return textView;
    }

}
