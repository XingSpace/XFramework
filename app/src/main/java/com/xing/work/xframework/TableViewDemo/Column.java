package com.xing.work.xframework.TableViewDemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.filter.IFilterableModel;
import com.evrencoskun.tableview.sort.ISortableModel;

public class Column implements ISortableModel, IFilterableModel {

    private String id,keyword;
    private Object data;

    public Column(String id,Object data){
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
}
