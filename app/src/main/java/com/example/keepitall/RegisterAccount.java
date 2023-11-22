package com.example.keepitall;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * Class used to for the RegisterAccount dialog, which handles the registration of a new account
 * This dialog is called from the loginActivity
 * error checking is done in this class and ensures that the user enters valid information
 * once the user enters valid information, the user is added to the database
 */
public class RegisterAccount extends AppCompatDialogFragment {

    // private variables
    private final KeepItAll keepItAll = KeepItAll.getInstance();
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
                        PrintValidRegistrationToast();
                        if (ValidRegistration()) {
                            RegisterNewUser();
                            dialog.dismiss();
                        }
                    }
                });
                // make the "clear" button also not dismiss the dialog
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Clear the input fields
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

    /**
     * Checks if the registration is valid by checking each part of the registration
     * and displaying a toast message if it is not valid
     * @return - true if the registration is valid, false otherwise
     */
    public boolean ValidRegistration(){
        // First check if any part of the registration is empty, and if so, display a toast message
        if(username.isEmpty() && password.isEmpty() && confirmPassword.isEmpty() && email.isEmpty()){
            return false;
        }
        // Check each specific part of the registration
        if(username.isEmpty()){
            return false;
        }
        if(password.isEmpty()){
            return false;
        }
        if(confirmPassword.isEmpty()){
            return false;
        }
        if(email.isEmpty()){
            return false;
        }
        // Check if the password and confirm password match
        if(!password.equals(confirmPassword)){
            return false;
        }
        // make sure the username is not already taken
        if(!keepItAll.isUsernameUnique(username)){
            return false;
        }
        // make sure the username is within the correct length (3-15 characters)
        if(username.length() < 3 || username.length() > 15){
            return false;
        }
        return true;
    }

    /**
     * Checks if the registration is valid by checking each part of the registration and prints a Toast for it
     * This is seperate from the ValidRegistration method because we want to display a toast message for each
     */
    private void PrintValidRegistrationToast(){
        // First check if any part of the registration is empty, and if so, display a toast message
        if(username.isEmpty() && password.isEmpty() && confirmPassword.isEmpty() && email.isEmpty()){
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
        // Check each specific part of the registration
        if(username.isEmpty()){
            Toast.makeText(getContext(), "Please enter a username", Toast.LENGTH_SHORT).show();
        }
        if(password.isEmpty()){
            Toast.makeText(getContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
        }
        if(confirmPassword.isEmpty()){
            Toast.makeText(getContext(), "Please confirm your password", Toast.LENGTH_SHORT).show();
        }
        if(email.isEmpty()){
            Toast.makeText(getContext(), "Please enter an email", Toast.LENGTH_SHORT).show();
        }
        // Check if the password and confirm password match
        if(!password.equals(confirmPassword)){
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
        // make sure the username is not already taken
        if(!keepItAll.isUsernameUnique(username)){
            Toast.makeText(getContext(), "Username already taken", Toast.LENGTH_SHORT).show();
        }
        // make sure the username is within the correct length (3-15 characters)
        if(username.length() < 3 || username.length() > 15){
            Toast.makeText(getContext(), "Username must be between 3 and 15 characters", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getContext(), "Account created!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Creates a new user and adds it to the database by calling the addUser method from KeepItAll
     */
    private void RegisterNewUser(){
        // creates a new user with the given information
        User newUser = new User(username, password, email);
        // adds the user to the database
        keepItAll.addUser(newUser);
    }

    // Getters and setters
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
