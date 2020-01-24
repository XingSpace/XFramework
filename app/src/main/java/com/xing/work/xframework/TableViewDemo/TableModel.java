package com.xing.work.xframework.TableViewDemo;

import java.util.ArrayList;
import java.util.List;

public class TableModel {

    private List<Column> mColumnList = new ArrayList<>();
    private List<Row> mRowList = new ArrayList<>();
    private List<List<Cell>> mCellList = new ArrayList<>();

    private int ColumnCount = 5;
    private int RowCount = 5;

    public TableModel(int column,int row){
        setColumn(column);
        setRow(row);
    }

    public void setColumn(int count){
        ColumnCount = count;
    }

    public void setRow(int count){
        RowCount = count;
    }

    public TableModel create(){
        for (int i=0;i<ColumnCount;i++) {
            Column column = new Column("column"+i,i);
            mColumnList.add(column);
        }
        for (int i=0;i<RowCount;i++) {
            Row row = new Row("row"+i,i);
            mRowList.add(row);
        }
        for (int i=0;i<RowCount;i++) {
            List<Cell> list = new ArrayList<>();
            for (int j=0;j<ColumnCount;j++){
                String data = "#"+((i*ColumnCount)+j)+"号电机";
                Cell cell = new Cell("cell"+i+"-"+j,data);
                list.add(cell);
            }
            mCellList.add(list);
        }
        return this;
    }

    public List<Column> getColumnList(){
        return mColumnList;
    }

    public List<Row> getRowList(){
        return mRowList;
    }

    public List<List<Cell>> getCellList(){
        return mCellList;
    }

}
