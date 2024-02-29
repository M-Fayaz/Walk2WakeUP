package com.sif.alarmwalk2wakeup.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AlarmRepository {
    private com.sif.alarmwalk2wakeup.data.AlarmDao alarmDao;
    private LiveData<List<com.sif.alarmwalk2wakeup.data.Alarm>> alarmsLiveData;

    public AlarmRepository(Application application) {
        com.sif.alarmwalk2wakeup.data.AlarmDatabase db = com.sif.alarmwalk2wakeup.data.AlarmDatabase.getDatabase(application);
        alarmDao = db.alarmDao();
        alarmsLiveData = alarmDao.getAlarms();
    }

    public void insert(com.sif.alarmwalk2wakeup.data.Alarm alarm) {
        com.sif.alarmwalk2wakeup.data.AlarmDatabase.databaseWriteExecutor.execute(() -> {
            alarmDao.insert(alarm);
        });
    }

    public void update(com.sif.alarmwalk2wakeup.data.Alarm alarm) {
        com.sif.alarmwalk2wakeup.data.AlarmDatabase.databaseWriteExecutor.execute(() -> {
            alarmDao.update(alarm);
        });
    }

    public LiveData<List<com.sif.alarmwalk2wakeup.data.Alarm>> getAlarmsLiveData() {
        return alarmsLiveData;
    }
}
