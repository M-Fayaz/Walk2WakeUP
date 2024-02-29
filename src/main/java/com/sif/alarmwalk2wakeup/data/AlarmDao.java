package com.sif.alarmwalk2wakeup.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlarmDao {
    @Insert
    void insert(com.sif.alarmwalk2wakeup.data.Alarm alarm);

    @Query("DELETE FROM alarm_table")
    void deleteAll();

    @Query("SELECT * FROM alarm_table ORDER BY created ASC")
    LiveData<List<com.sif.alarmwalk2wakeup.data.Alarm>> getAlarms();

    @Update
    void update(com.sif.alarmwalk2wakeup.data.Alarm alarm);
}
