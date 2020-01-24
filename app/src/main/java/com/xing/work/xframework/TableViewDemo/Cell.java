package com.xing.work.xframework.TableViewDemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.filter.IFilterableModel;
import com.evrencoskun.tableview.sort.ISortableModel;

public class Cell implements ISortableModel, IFilterableModel {

    private String id,keyword;
    private Object data;

    private boolean isChecked = false;

    public Cell(String id,Object data){
        this.id = id;
        this.data = data;
        keyword = String.valueOf(data);
    }

    public void setFilterableKeyword(String keyword){
        this.keyword = keyword;
    }

    @NonNull
    @Override
    public String getFilterableKeyword() {
        return keyword;
    }

    @NonNull
    @Override
    public String getId() {
        return id;
    }

    @Nullable
    @Override
    public Object getContent() {
        return data;
    }

    public boolean isCheck(){
        return isChecked;
    }

    public void setChecked(boolean b){
        isChecked = b;
    }
}
