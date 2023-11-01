package com.example.keepitall;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class RegisterAccount extends AppCompatDialogFragment {

    // Sets up the variables that will be used within the class
    private RegisterAccountListener Listener;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextEmail;

    private String username;
    private String password;
    private String confirmPassword;
    private String email;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.register_account_layout, null);

        // Setup the variables
        editTextUsername = view.findViewById(R.id.Register_Username_Input);
        editTextPassword = view.findViewById(R.id.Register_Password_Input);
        editTextConfirmPassword = view.findViewById(R.id.Register_Confirm_Password_Input);
        editTextEmail = view.findViewById(R.id.Register_Email_Input);


        builder.setView(view)
                .setTitle("Create Account")
                // Creates the cancel button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                // Creates the confirm button
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         username = editTextUsername.getText().toString();
                        password = editTextPassword.getText().toString();
                        confirmPassword = editTextConfirmPassword.getText().toString();
                        email = editTextEmail.getText().toString();
                        Listener.RegisterAccount(username, password, email);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            Listener = (RegisterAccountListener) context;
        } catch (ClassCastException e) {
            //
        }
    }


    // Function to ensure the input register is vald
    private void ValidRegistration(){

    }


    // Creates the interface we will use to call this on the main class
    public interface RegisterAccountListener{
        void RegisterAccount(String username, String password, String email);
    }
}
