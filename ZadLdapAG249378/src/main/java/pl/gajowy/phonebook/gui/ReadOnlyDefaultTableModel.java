package pl.gajowy.phonebook.gui;

import javax.swing.table.DefaultTableModel;

class ReadOnlyDefaultTableModel extends DefaultTableModel {
    final Class[] types;

    public ReadOnlyDefaultTableModel(String[] columnNames, Class[] columnTypes) {
        super(new Object[][]{}, columnNames);
        setColumnCount(columnNames.length);
        this.types = columnTypes;
    }

    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
