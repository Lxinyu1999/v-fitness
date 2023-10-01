package com.group77.fitness.fitnesscore.dao.impl;

import com.group77.fitness.fitnesscore.dao.RecordDAO;
import com.group77.fitness.fitnesscore.json.JsonDataFile;
import com.group77.fitness.fitnesscore.json.JsonReader;
import com.group77.fitness.fitnesscore.json.JsonWriter;
import com.group77.fitness.fitnesscore.vo.Record;

import java.util.ArrayList;

public class RecordDAOImpl implements RecordDAO {
    private ArrayList<Record> recordList;

    public RecordDAOImpl() throws Exception {
        this.readRecordJson();
    }

    public void readRecordJson() throws Exception {
        this.recordList = (ArrayList<Record>) JsonReader.readJsonToObject(JsonDataFile.RECORD);
    }

    private void saveRecordJson() throws Exception {
        JsonWriter.writeObjectToJson(JsonDataFile.RECORD, this.recordList);
        this.readRecordJson();  // reread
    }

    @Override
    public boolean addRecord(Record record) throws Exception {
        this.recordList.add(record);
        this.saveRecordJson();
        return true;
    }

    @Override
    public boolean updateRecord(Record record) throws Exception {
        String accountID = record.getAccountID();

        for (int i = 0; i < this.recordList.size(); i++) {
            if (this.recordList.get(i).getAccountID().equals(accountID)) {
                this.recordList.set(i, record);
                this.saveRecordJson();
                return true;
            }
        }

        return false;
    }

    @Override
    public Record getRecordByAccountID(String accountID) {
        for (Record record : this.recordList) {
            if (record.getAccountID().equals(accountID)) return record;
        }

        return null;
    }
}
