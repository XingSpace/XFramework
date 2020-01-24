package com.xing.work.xframework.TableViewDemo;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.xing.work.xframework.R;

public class ColumnHolder extends AbstractViewHolder {

    public Button mButton;
    public TextView mTextView;

    public ColumnHolder(@NonNull View itemView) {
        super(itemView);
        mButton = itemView.findViewById(R.id.btn);
        mTextView = itemView.findViewById(R.id.num);
    }
}
