package com.example.android.gatheraround;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.gatheraround.custom_classes.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileDescriptor;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    
    Button loginButton, signUpButton;
    EditText loginEmailEditText, loginPasswordEditText, emailEditText, passwordEditText, confirmPasswordEditText, signUpNameEditText;
    ImageView selectImageButton;

    boolean isImageSelected;

    UserProfile profile;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        loginEmailEditText = (EditText)findViewById(R.id.loginEmailEditText);
        loginPasswordEditText = (EditText)findViewById(R.id.loginPasswordEditText);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText)findViewById(R.id.signUpPasswordConfirmEditText);
        signUpNameEditText = (EditText)findViewById(R.id.signUpNameEditText);

        signUpButton = (Button)findViewById(R.id.signUpButton);
        loginButton = (Button)findViewById(R.id.loginButton);

        selectImageButton = (ImageView)findViewById(R.id.selectImageButton);

        setBlankImageToSelectImage();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });
    }

    public void login(){

        SpannableStringBuilder builder = (SpannableStringBuilder)loginEmailEditText.getText();
        final String email = builder.toString();
        builder = (SpannableStringBuilder)loginPasswordEditText.getText();
        final String password = builder.toString();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, InitialActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "Seems failed to login. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signUp(){

        SpannableStringBuilder builder = (SpannableStringBuilder)emailEditText.getText();
        final String email = builder.toString();
        builder = (SpannableStringBuilder)passwordEditText.getText();
        final String password = builder.toString();
        builder = (SpannableStringBuilder)confirmPasswordEditText.getText();
        final String confirm = builder.toString();
        builder = (SpannableStringBuilder)signUpNameEditText.getText();
        final String name = builder.toString();

        if(password.equals(confirm)){
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Successfully created a new account", Toast.LENGTH_SHORT).show();

                        profile = new UserProfile(email, name);
                        DataSenderToServer sender = new DataSenderToServer();

                        FirebaseUser user = auth.getCurrentUser();

                        sender.addNewUser(user.getUid(), profile);

                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, InitialActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Failed created a new account", Toast.LENGTH_SHORT).show();

                        Log.i("Failed", "Failed:" + task.getException().getMessage());
                    }
                }
            });
        }else{
            Toast.makeText(LoginActivity.this, "Password and confirmation does not match. Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setBlankImageToSelectImage(){
        selectImageButton.setImageResource(R.drawable.button_add_image);
        isImageSelected = false;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException{
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        // TODO: 2017/11/07 requestCodeを理解する
        Uri uri = null;
        if(resultData != null){
            uri = resultData.getData();

            try{
                Bitmap bitmap = getBitmapFromUri(uri);
                selectImageButton.setImageBitmap(bitmap);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
