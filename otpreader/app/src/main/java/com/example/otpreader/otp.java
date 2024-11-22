package com.example.otpreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.telephony.TelephonyCallback;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

public class otp extends BroadcastReceiver {


    private static EditText editText;

    public void setEditText(EditText editText){
        otp.editText = editText;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage smsMessage: smsMessages){
            String message_body = String.valueOf(smsMessages.getClass());
            String getOtp = message_body.split(":")[1];
            editText.setText(getOtp);
        }

    }
}
