package com.example.keepitall;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class RegisterAccount extends AppCompatDialogFragment {

    // Sets up the variables that will be used within the class
    private KeepItAll keepItAll;
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
        ///TODO: This is where the keepItAll object is created temporarily
        keepItAll = new KeepItAll();

        // Set the default values of the variables
        // This is used for if the user entered some information and then pressed cancel
        // or if the user entered some information and then pressed confirm but the registration failed
        editTextUsername.setText(username);
        editTextPassword.setText(password);
        editTextConfirmPassword.setText(confirmPassword);
        editTextEmail.setText(email);

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
                        // Check for a valid registration
                        ValidRegistration();
                        //Listener.RegisterAccount(username, password, email);
                    }
                })
                .setIcon(R.drawable.user_icon)
                .setMessage("Please enter your details below")
                .setNeutralButton("clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // We override this later to prevent the dialog from closing
                    }
                });


        final AlertDialog dialog = builder.create();

        // Customize the behavior of the Confirm button
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                // Set the confirm button to not dismiss the dialog,
                // unless we validate the input
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        username = editTextUsername.getText().toString();
                        password = editTextPassword.getText().toString();
                        confirmPassword = editTextConfirmPassword.getText().toString();
                        email = editTextEmail.getText().toString();

                        // Check for a valid registration
                        if (ValidRegistration()) {
                            // You can close the dialog here or do whatever is needed
                            dialog.dismiss();
                            //Listener.RegisterAccount(username, password, email);
                        }
                    }
                });
                // make the "clear" button also not dismiss the dialog
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        username = "";
                        password = "";
                        confirmPassword = "";
                        email = "";
                        editTextUsername.setText(username);
                        editTextPassword.setText(password);
                        editTextConfirmPassword.setText(confirmPassword);
                        editTextEmail.setText(email);
                    }
                });
            }
        });
        return dialog;
    }
    // make it so when the confirm button is pressed, the dialuge isnt dismissed
    // and the user is told what they did wrong


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            Listener = (RegisterAccountListener) context;
        } catch (ClassCastException e) {
            //
        }
    }

    private boolean ValidRegistration(){
        // First check if any part of the registration is empty, and if so, display a toast message
        if(username.isEmpty() && password.isEmpty() && confirmPassword.isEmpty() && email.isEmpty()){
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check each specific part of the registration
        if(username.isEmpty()){
            Toast.makeText(getContext(), "Please enter a username", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.isEmpty()){
            Toast.makeText(getContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(confirmPassword.isEmpty()){
            Toast.makeText(getContext(), "Please confirm your password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.isEmpty()){
            Toast.makeText(getContext(), "Please enter an email", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if the password and confirm password match
        if(!password.equals(confirmPassword)){
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        // make sure the username is not already taken
        if(!keepItAll.isUsernameUnique(username)){
            Toast.makeText(getContext(), "Username already taken", Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(getContext(), "Account created!", Toast.LENGTH_SHORT).show();
        return true;
    }


    // Creates the interface we will use to call this on the main class
    public interface RegisterAccountListener{
        void RegisterAccount(String username, String password, String email);
    }
}
