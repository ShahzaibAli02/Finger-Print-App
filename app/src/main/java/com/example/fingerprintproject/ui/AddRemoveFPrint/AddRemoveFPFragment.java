package com.example.fingerprintproject.ui.AddRemoveFPrint;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;


import com.example.fingerprintproject.databinding.FragmentAddRemoveFingeprintBinding;


public class AddRemoveFPFragment extends Fragment
{

    private FragmentAddRemoveFingeprintBinding binding;

    public View onCreateView (@NonNull LayoutInflater inflater ,
                              ViewGroup container , Bundle savedInstanceState)
    {


        binding = FragmentAddRemoveFingeprintBinding.inflate(inflater , container , false);
        View root = binding.getRoot();



        View.OnClickListener onClickListener=new View.OnClickListener()
        {
            @Override
            public void onClick (View v) {
                startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
            }
        };
        binding.btnAdd.setOnClickListener(onClickListener);
        binding.btnRemove.setOnClickListener(onClickListener);
        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }
}