package com.example.fingerprintproject.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel
{

    private MutableLiveData <String> mText;
    public  int failed_count=0;
    public  static  boolean isLocked=false;
    public HomeViewModel () {
        mText = new MutableLiveData <>();
        mText.setValue("This is home fragment");
    }

    public LiveData <String> getText () {
        return mText;
    }
}