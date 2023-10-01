package com.group77.fitness.fitnesscore.dao;

import com.group77.fitness.fitnesscore.vo.Record;

public interface RecordDAO {
    boolean addRecord(Record record) throws Exception;
    boolean updateRecord(Record record) throws Exception;

    Record getRecordByAccountID(String accountID);
}
