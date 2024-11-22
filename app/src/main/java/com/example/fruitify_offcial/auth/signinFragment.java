package com.example.fruitify_offcial.auth;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.fruitify_offcial.R;
import com.example.fruitify_offcial.databinding.FragmentSigninBinding;


public class signinFragment extends Fragment {

    FragmentSigninBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSigninBinding.inflate(inflater,container,false);
        setStatusBarColor();
        getUserNumber();
        onContinueButtonClick();
        return binding.getRoot();
    }

    public void onContinueButtonClick(){

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = binding.etUserNumber.getText().toString();
                if(number.length() != 10){
                    Toast.makeText(requireContext(), "Please Enter vaild Phone Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("number",number);
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.action_signinFragment_to_OTPFragment,bundle);
                }
            }
        });
    }

    public void getUserNumber(){
        binding.etUserNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence number, int start, int before, int count) {
                int len = number.length();
                if(len == 10){
                    binding.loginBtn.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.green));
                }else {
                    binding.loginBtn.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yellow));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setStatusBarColor(){
        if(getActivity() != null && getActivity().getWindow() != null){
            Window window = getActivity().getWindow();
            int stautsBarColor = getResources().getColor(R.color.yellow);
            window.setStatusBarColor(stautsBarColor);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}