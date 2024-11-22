package com.example.fruitify_offcial;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.fruitify_offcial.databinding.ProgessDialogBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    private static AlertDialog dialog;

    public static void showDialog(Context context, String message) {
        ProgessDialogBinding progessDialogBinding = ProgessDialogBinding.inflate(LayoutInflater.from(context));
        TextView textViewMessage = progessDialogBinding.progressMessage;
        textViewMessage.setText(message);

        // Create and show the dialog
        dialog = new AlertDialog.Builder(context)
                .setView(progessDialogBinding.getRoot())
                .setCancelable(false)
                .create();
        dialog.show();
    }

    public static void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static String getCurrentUserID() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            // Optionally log or handle the case where the user is not authenticated
            return "";
        }
    }

    public static String getCurrentDate() {
        LocalDate currentTime = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return currentTime.format(formatter);
    }
}
