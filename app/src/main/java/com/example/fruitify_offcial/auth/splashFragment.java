package com.example.fruitify_offcial.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.fruitify_offcial.R;
import com.example.fruitify_offcial.activity.UsersMainActivity;
import com.example.fruitify_offcial.databinding.FragmentSplashBinding;
import com.example.fruitify_offcial.viewModels.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class splashFragment extends Fragment {
    AuthViewModel viewModel;
    FragmentSplashBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding = FragmentSplashBinding.inflate(inflater, container, false);
        setStatusBarColor();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    startActivity(new Intent(requireActivity(), UsersMainActivity.class));
                    requireActivity().finish();
                } else {
                    NavHostFragment.findNavController(splashFragment.this)
                            .navigate(R.id.action_splashFragment_to_signinFragment);
                }
            }
        }, 3000);
        return binding.getRoot();
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