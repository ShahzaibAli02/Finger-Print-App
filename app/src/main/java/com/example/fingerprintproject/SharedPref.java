package com.example.fingerprintproject;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SharedPref
{

    public static  void addrecord(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("com.example.fingerprintproject",Context.MODE_PRIVATE);
       String current_date_time=new SimpleDateFormat("EEE, d MMM yyyy h:mm a", Locale.getDefault()).format(new Date());
        List <String> record = getRecord(context);
        record.add(current_date_time);
        sharedPreferences.edit().putString("Records",new Gson().toJson(record)).apply();
    }

    public static List<String> getRecord (Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("com.example.fingerprintproject",Context.MODE_PRIVATE);
        String records = sharedPreferences.getString("Records" , "");
        if(records.isEmpty())
        {
            return  new ArrayList<>();
        }
        else
        {
            Type typeToken=new TypeToken<List<String>>(){}.getType();
            return new Gson().fromJson(records,typeToken);
        }
    }

}
