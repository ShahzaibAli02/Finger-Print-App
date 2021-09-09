package com.example.fingerprintproject.ui.home;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import static androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fingerprintproject.R;
import com.example.fingerprintproject.SharedPref;
import com.example.fingerprintproject.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



public class HomeFragment extends Fragment
{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    TextView txtMessage;
    TextView txtCounter;
    Button btnClick;

    BiometricPrompt biometricPrompt;
    private Executor executor = Executors.newSingleThreadExecutor();
    public View onCreateView (@NonNull LayoutInflater inflater ,
                              ViewGroup container , Bundle savedInstanceState)
    {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater , container , false);
        View root = binding.getRoot();

        txtMessage= binding.txtMessage;
        txtCounter= binding.txtCounter;
        btnClick= binding.btnClick;
        btnClick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                {
                    authenticate();
                }
            }
        });
        if(HomeViewModel.isLocked)
        {
            setLockedMessage();
        }
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public  void  authenticate ()
    {

         BiometricPrompt.AuthenticationCallback callback=new
                BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        if(errorCode==ERROR_NEGATIVE_BUTTON && biometricPrompt!=null)
                            biometricPrompt.cancelAuthentication();
                        snack((String) errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {

                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run () {
                                txtMessage.setText("Door is Unlocked");
                                homeViewModel.failed_count =0;
                                SharedPref.addrecord(getActivity());
                            }
                        });


                    }

                    @Override
                    public void onAuthenticationFailed()
                    {

                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run () {
                                if(homeViewModel.failed_count==3 || HomeViewModel.isLocked)
                                {
                                    homeViewModel.failed_count =0;
                                    HomeViewModel.isLocked=true;
                                    setLockedMessage();
                                    biometricPrompt.cancelAuthentication();
                                }
                                else
                                {
                                    homeViewModel.failed_count++;
                                    txtMessage.setText("Door is Locked");
                                    Toast.makeText(getActivity(),"FAILED",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                };


        biometricPrompt=new BiometricPrompt(this,executor,callback);
        BiometricPrompt.PromptInfo promptInfo = buildBiometricPrompt();
        biometricPrompt.authenticate(promptInfo);
    }

    private BiometricPrompt.PromptInfo buildBiometricPrompt()
    {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setSubtitle("FingerPrint Authentication")
                .setDescription("Please place your finger on the sensor to unlock")
                .setNegativeButtonText("Cancel")
                .build();

    }
  /*  private void checkAndAuthenticate(){
        BiometricManager biometricManager=BiometricManager.from(getActivity());
        switch (biometricManager.canAuthenticate())
        {
            case BiometricManager.BIOMETRIC_SUCCESS:
                BiometricPrompt.PromptInfo promptInfo = buildBiometricPrompt();
                biometricPrompt.authenticate(promptInfo);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                snack("Biometric Authentication currently unavailable");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                snack("Your device doesn't support Biometric Authentication");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                snack("Your device doesn't have any fingerprint enrolled");
                break;
        }
    }

   */

    private void snack (String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content),message, BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    private void setLockedMessage ()
    {
        txtMessage.setText("Disabled Please Try Again After");
        txtCounter.setText("30");
        txtCounter.setVisibility(View.VISIBLE);
        btnClick.setVisibility(View.GONE);
        new CountDownTimer(30000,1000)
        {
            @Override
            public void onTick (long millisUntilFinished)
            {
                txtCounter.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish ()
            {
                txtMessage.setText("Please Verify Your Finger Print");
                txtCounter.setVisibility(View.GONE);
                btnClick.setVisibility(View.VISIBLE);
                HomeViewModel.isLocked=false;
            }
        }.start();

    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }
}