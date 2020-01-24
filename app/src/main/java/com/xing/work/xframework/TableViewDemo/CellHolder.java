package com.xing.work.xframework.TableViewDemo;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.xing.work.xframework.R;

public class CellHolder extends AbstractViewHolder {

    public TextView mTextView;
    public CheckBox mChecked;

    private boolean isChecked = false;

    public CellHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.aisle_number);
        mChecked = itemView.findViewById(R.id.aisle_check);
    }

    public boolean isCheck(){
        return isChecked;
    }

    public void setChecked(boolean b){
        isChecked = b;
        mChecked.setChecked(b);
    }
}
