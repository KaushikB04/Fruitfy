package com.example.fruitify_offcial.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.fruitify_offcial.R;
import com.example.fruitify_offcial.Utils;
import com.example.fruitify_offcial.activity.UsersMainActivity;
import com.example.fruitify_offcial.databinding.FragmentOTPBinding;
import com.example.fruitify_offcial.models.User;
import com.example.fruitify_offcial.viewModels.AuthViewModel;

public class OTPFragment extends Fragment {

    private AuthViewModel viewModel;
    private FragmentOTPBinding binding;
    private String userNumber;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOTPBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        getUserNumber();
        customizeEnteringOTP();
        onBackButtonClick();
        sendOTP();
        onLoginButtonClick();
    }

    private void onLoginButtonClick() {
        binding.loginOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showDialog(requireContext(),"Signing you..");
                EditText[] editTexts = new EditText[]{
                        binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4, binding.etOtp5, binding.etOtp6
                };
                StringBuilder otpBuilder =new StringBuilder();
                for (EditText editText: editTexts){
                    otpBuilder.append(editText.getText().toString());
                }
                String otp = otpBuilder.toString();
                if(otp.length() < editTexts.length){
                    Utils.hideDialog();
                    Toast.makeText(requireContext(), "please enter right otp", Toast.LENGTH_SHORT).show();
                }
                else {
                    verifyOTP(otp);
                }
             }
        });
    }

    private void verifyOTP(String otp) {
        User user = new User();
        user.setUid(null);
        user.setUserPhoneNumber(userNumber);
        user.setUserAddress(" ");

        viewModel.signInWithPhoneAuthCredential(otp,userNumber,user);
        viewModel.get_isSignInSuccessfully().observe(getViewLifecycleOwner(),isSignInSuccessfully ->{
            if(isSignInSuccessfully){
                Utils.hideDialog();
                Toast.makeText(requireContext(), "Logged In..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireActivity(), UsersMainActivity.class));
                requireActivity().finish();
            }
        });
    }

    private void sendOTP() {
        Utils.showDialog(requireContext(), "Sending OTP...");

        viewModel.sendOTP(userNumber,requireActivity());
        viewModel.get_otpSent().observe(getViewLifecycleOwner(), otpSent -> {
            // Hide the dialog once the OTP sending result is received

            if (otpSent) {
                Utils.hideDialog();
                Toast.makeText(requireContext(), "OTP sent successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void onBackButtonClick() {
        binding.tpOtpFragment.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_OTPFragment_to_signinFragment);
        });
    }

    private void customizeEnteringOTP() {
        final EditText[] editTexts = new EditText[]{
                binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4, binding.etOtp5, binding.etOtp6
        };

        for (int i = 0; i < editTexts.length; i++) {
            final int index = i;
            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < editTexts.length - 1) {
                        editTexts[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        editTexts[index - 1].requestFocus();
                    }
                }
            });
        }
    }

    private void getUserNumber() {
        if (getArguments() != null) {
            userNumber = getArguments().getString("number");
            if (!TextUtils.isEmpty(userNumber)) {
                binding.tvUserNumber.setText(userNumber);
            } else {
                Toast.makeText(requireContext(), "User number not provided", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
