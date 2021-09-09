package com.example.fingerprintproject.ui.ShowRecords;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShowRecordsViewModel extends ViewModel
{

    private MutableLiveData <List<String>> mRecords;

    public ShowRecordsViewModel ()
    {
        mRecords = new MutableLiveData <>();
        mRecords.setValue(new ArrayList <>());
    }
    public  void  setRecords(List<String> records)
    {
        mRecords.setValue(records);
    }

    public LiveData <List<String>> getRecords () {
        return mRecords;
    }
}