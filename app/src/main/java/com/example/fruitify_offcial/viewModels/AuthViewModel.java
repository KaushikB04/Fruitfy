    package com.example.fruitify_offcial.viewModels;

    import static android.content.ContentValues.TAG;

    import android.app.Activity;
    import android.util.Log;

    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.MutableLiveData;
    import androidx.lifecycle.ViewModel;

    import com.example.fruitify_offcial.Utils;
    import com.example.fruitify_offcial.models.User;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.FirebaseException;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.auth.PhoneAuthCredential;
    import com.google.firebase.auth.PhoneAuthOptions;
    import com.google.firebase.auth.PhoneAuthProvider;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.ktx.Firebase;

    import org.checkerframework.checker.nullness.qual.NonNull;

    import java.util.concurrent.TimeUnit;

    public class AuthViewModel extends ViewModel {

        private FirebaseAuth mAuth;
        private MutableLiveData<String> _verificationID = new MutableLiveData<>(null);

        private MutableLiveData<Boolean> _otpSent = new MutableLiveData<Boolean>(false);
        MutableLiveData<Boolean> otpSent = _otpSent;

        private MutableLiveData<Boolean> _isSignInSuccessfully = new MutableLiveData<Boolean>(false);
        MutableLiveData<Boolean> isSignInSuccessfully = _isSignInSuccessfully;

        public MutableLiveData<Boolean> get_otpSent() {
            return _otpSent;
        }

        private MutableLiveData<Boolean> _isACurrentUser = new MutableLiveData<Boolean>(false);
        MutableLiveData<Boolean> isACurrentUser = _isACurrentUser;
        public MutableLiveData<Boolean> get_isACurrentUser() {
            return _isACurrentUser;
        }

        private void checkIfCurrentUserExists() {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                _isACurrentUser.setValue(true);
            }
        }

        public MutableLiveData<Boolean> get_isSignInSuccessfully() {
            return _isSignInSuccessfully;
        }

        public AuthViewModel() {
            mAuth = FirebaseAuth.getInstance();
            checkIfCurrentUserExists();
        }

        public void sendOTP(String userNumber, Activity activity){
            PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {

                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       PhoneAuthProvider.@NonNull ForceResendingToken token) {
                    _verificationID.setValue(verificationId);
                    _otpSent.setValue(true);
                }
            };

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber("+91"+ userNumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(activity)                 // (optional) Activity for callback binding
                            // If no activity is passed, reCAPTCHA verification can not be used.
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

        }

        public void signInWithPhoneAuthCredential(String otp, String userNumber, User user) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(_verificationID.getValue().toString(), otp);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            user.setUid(Utils.getCurrentUserID());
                            if (task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(user.getUid()).setValue(user);
                                _isSignInSuccessfully.setValue(true);
                            } else {

                            }
                        }
                    });
        }

    }
