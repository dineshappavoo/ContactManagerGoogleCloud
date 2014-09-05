package com.swinggui;


/**
 * 
 * User: Dinesh Appavoo
 */
public class Record {
    private final Object[]  values;

    public Record(Object[] values) {
        this.values = values;
    }

    public int getSize() {
        return values.length;
    }

    public Object getValue(int i) {
        return values[i];
    }
}
