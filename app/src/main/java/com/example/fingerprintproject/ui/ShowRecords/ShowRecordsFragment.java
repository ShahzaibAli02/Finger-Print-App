package com.example.fingerprintproject.ui.ShowRecords;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fingerprintproject.R;
import com.example.fingerprintproject.SharedPref;
import com.example.fingerprintproject.databinding.FragmentShowRecordsBinding;

import java.util.List;

public class ShowRecordsFragment extends Fragment
{

    private ShowRecordsViewModel showRecordsViewModel;
    private FragmentShowRecordsBinding binding;
    ArrayAdapter adapter;
    public View onCreateView (@NonNull LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState)
    {
        showRecordsViewModel = new ViewModelProvider(this).get(ShowRecordsViewModel.class);
        binding              = FragmentShowRecordsBinding.inflate(inflater , container , false);
        View root = binding.getRoot();
        final ListView listView = binding.listView;

        showRecordsViewModel.getRecords().observe(getViewLifecycleOwner() , new Observer <List <String>>()
        {
            @Override
            public void onChanged (List <String> strings)
            {
                adapter=new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item,strings);
                listView.setAdapter(adapter);

            }
        });
        showRecordsViewModel.setRecords(SharedPref.getRecord(getActivity()));
        return root;
    }


    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }
}