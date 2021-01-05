package com.example.hw1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopTenRecords implements Serializable {

    private ArrayList<Record> records = new ArrayList<>();
    public TopTenRecords()  {
    }

    public TopTenRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void addRecord(Record record){
            this.records.add(record);
        }

    public Record getRecord(int i){
        return records.get(i);
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

}
