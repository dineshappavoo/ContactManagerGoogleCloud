package com.swinggui;


import javax.swing.table.AbstractTableModel;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *@author Dany
 */

public class ResultSetTableModel extends AbstractTableModel {
    private final String[] searchContacts = new String[]{"First Name", "Last Name", "Middle Initial", "Phone"};

    private List<Record> records;


    public ResultSetTableModel(List<Record> records) {
        if (records != null) {
            this.records = records;
        } else {
            this.records = new ArrayList<Record>();
        }

    }

    public int getRowCount() {
        return records.size();
    }

    public int getColumnCount() {
                return 4;
    }

    public Object getValue(int row, int column) {
        return records.get(row).getValue(column);
    }

    public String getColumnName(int col) {
            return searchContacts[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex > records.size()) {
            return null;
        }
        return records.get(rowIndex).getValue(columnIndex);
    }

    public List<Record> getRecords() {
        return this.records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }


}
