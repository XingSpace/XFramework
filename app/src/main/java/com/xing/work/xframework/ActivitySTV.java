package com.xing.work.xframework;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.SimpleTableViewListener;
import com.xing.app.myutils.Utils.LogUtil;
import com.xing.work.xframework.TableViewDemo.Cell;
import com.xing.work.xframework.TableViewDemo.CellHolder;
import com.xing.work.xframework.TableViewDemo.TableModel;
import com.xing.work.xframework.TableViewDemo.TestTableAdapter;

public class ActivitySTV extends ActivityBase {

    private TableView mTableView;
    private TestTableAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stv);
    }

    @Override
    public void findViews() {
        mTableView = findViewById(R.id.table);

        TableModel tableModel = new TableModel(10,10).create();
        adapter = new TestTableAdapter();
        mTableView.setAdapter(adapter);
//        mTableView.setIgnoreSelectionColors(true);

        adapter.setAllItems(tableModel.getColumnList(),tableModel.getRowList(),tableModel.getCellList());

        mTableView.setTableViewListener(new SimpleTableViewListener() {
            @Override
            public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
                LogUtil.e("列->"+column+"行->"+row);
                CellHolder cellHolder = (CellHolder) mTableView.getCellLayoutManager().getCellViewHolder(column,row);
                Cell cell = (Cell) mTableView.getAdapter().getCellItem(column,row);
                LogUtil.e("丧心病狂-----列->"+column+"行-》"+row);
                if (cell.isCheck()){
                    cell.setChecked(false);
                    if (cellHolder != null) {
                        cellHolder.setChecked(false);
                    }
                } else {
                    cell.setChecked(true);
                    if (cellHolder != null){
                        cellHolder.setChecked(true);
                    }
                }
            }

            @Override
            public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
                LogUtil.e("竖点击 列->"+adapter.getCountRow());
                for (int i=0;i<adapter.getCountRow();i++){
                    CellHolder cellHolder = (CellHolder) mTableView.getCellLayoutManager().getCellViewHolder(column,i);
                    Cell cell = (Cell) mTableView.getAdapter().getCellItem(column,i);
                    LogUtil.e("丧心病狂-----列->"+column+"行-》"+i);
                    if (cell.isCheck()){
                        cell.setChecked(false);
                        if (cellHolder != null) {
                            cellHolder.setChecked(false);
                        }
                    } else {
                        cell.setChecked(true);
                        if (cellHolder != null){
                            cellHolder.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
                LogUtil.e("行点击 列->"+adapter.getCountColumn());
                for (int i=0;i<adapter.getCountColumn();i++){
                    CellHolder cellHolder = (CellHolder) mTableView.getCellLayoutManager().getCellViewHolder(i,row);
                    Cell cell = (Cell) mTableView.getAdapter().getCellItem(i,row);
                    LogUtil.e("丧心病狂-----列->"+i+"行-》"+row);
                    if (cell.isCheck()){
                        cell.setChecked(false);
                        if (cellHolder != null) {
                            cellHolder.setChecked(false);
                        }
                    } else {
                        cell.setChecked(true);
                        if (cellHolder != null){
                            cellHolder.setChecked(true);
                        }
                    }
                }
            }
        });
    }


    @Override
    public void init() {
        setLeftOnClick(view -> finish());
    }
}
