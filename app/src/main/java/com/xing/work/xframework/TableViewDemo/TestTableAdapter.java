package com.xing.work.xframework.TableViewDemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.xing.work.xframework.R;

/**
 * TableView的适配类
 */
public class TestTableAdapter extends AbstractTableAdapter<Column,Row,Cell> {

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_holder,parent,false);
        return new CellHolder(view);
    }

    @Override
    public void onBindCellViewHolder(@NonNull AbstractViewHolder holder, @Nullable Cell cellItemModel, int columnPosition, int rowPosition) {
        int index = (rowPosition * getCountColumn()) + columnPosition+1;
        ((CellHolder)holder).mTextView.setText("#"+index+"电机");
        ((CellHolder)holder).setChecked(cellItemModel.isCheck());
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_holder,parent,false);
        return new ColumnHolder(view);
    }

    @Override
    public void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable Column columnHeaderItemModel, int columnPosition) {
        ((ColumnHolder)holder).mTextView.setText("L"+(columnPosition+1));
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_holder,parent,false);
        return new RowHolder(view);
    }

    @Override
    public void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable Row rowHeaderItemModel, int rowPosition) {
        ((RowHolder)holder).mTextView.setText("C"+(rowPosition+1));
    }

    @NonNull
    @Override
    public View onCreateCornerView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.corner_holder,parent,false);
    }

    public int getCountColumn(){
        return mColumnHeaderItems.size();
    }

    public int getCountRow(){
        return mRowHeaderItems.size();
    }

}
