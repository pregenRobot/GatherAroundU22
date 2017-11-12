package com.example.android.gatheraround;

import android.content.Context;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    
    Button loginButton, signUpButton;
    EditText loginEmailEditText, loginPasswordEditText, emailEditText, passwordEditText, confirmPasswordEditText, signUpNameEditText, signUpProfileEditText;
    ImageView selectProfileImageButton, selectBackgroundButton;

    View loginBackView;
    View signInFaceView;
    View loginCoordinate;
    Button switcher;

    CropImageView cropImageView;

    boolean isImageSelected, isEditingProfile;
    Uri profileUri, backgroundUri;

    UserProfile profile;

    private FirebaseAuth auth;

    static final int PROFILE_IMAGE_PICK_REQUEST_CODE = 0;
    static final int BACKGROUND_IMAGE_PICK_REQUEST_CODE = 1;

    static final String FILE_NAME_PROFILE_IMAGE = "profile.png";
    static final String FILE_NAME_BACKGROUND_IMAGE = "background.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBackView = findViewById(R.id.loginback);
        signInFaceView = findViewById(R.id.signface);
        loginCoordinate = findViewById(R.id.cordinateparent);

        switcher = findViewById(R.id.switcher);

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlipAnimation flipAnimation;
                if(loginBackView.getVisibility() == View.GONE){
                    flipAnimation = new FlipAnimation(loginBackView, signInFaceView);
                    flipAnimation.reverse();
                    loginCoordinate.startAnimation(flipAnimation);
                    switcher.setText("Create a new account?");
                }else if(signInFaceView.getVisibility() == View.GONE){
                    flipAnimation = new FlipAnimation(signInFaceView, loginBackView);
                    flipAnimation.reverse();
                    loginCoordinate.startAnimation(flipAnimation);
                    switcher.setText("Already have an account?");
                }
            }
        });


        auth = FirebaseAuth.getInstance();

        // for login
        loginEmailEditText = (EditText)findViewById(R.id.loginEmailEditText);
        loginPasswordEditText = (EditText)findViewById(R.id.loginPasswordEditText);
        // for sign up
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText)findViewById(R.id.signUpPasswordConfirmEditText);
        signUpNameEditText = (EditText)findViewById(R.id.signUpNameEditText);
        signUpProfileEditText = (EditText)findViewById(R.id.signUpEditProfileTextEditText);
        selectProfileImageButton = (ImageView)findViewById(R.id.selectImageButton);
        selectBackgroundButton = (ImageView)findViewById(R.id.selectBackgroundButton);

        loginButton = (Button)findViewById(R.id.loginButton);
        signUpButton = (Button)findViewById(R.id.signUpButton);

        cropImageView = (CropImageView)findViewById(R.id.cropImageView);

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

        selectProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, PROFILE_IMAGE_PICK_REQUEST_CODE);
            }
        });

        selectBackgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, BACKGROUND_IMAGE_PICK_REQUEST_CODE);
            }
        });
    }

    public void login(){

        SpannableStringBuilder builder = (SpannableStringBuilder)loginEmailEditText.getText();
        final String email = builder.toString();
        builder = (SpannableStringBuilder)loginPasswordEditText.getText();
        final String password = builder.toString();

        Toast.makeText(this, "Login on going", Toast.LENGTH_SHORT).show();

        if(!email.isEmpty() && !password.isEmpty()){
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
        } else{
            Toast.makeText(this, "Please fill up the form to login.", Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp() {

        SpannableStringBuilder builder = (SpannableStringBuilder) emailEditText.getText();
        final String email = builder.toString();
        builder = (SpannableStringBuilder) passwordEditText.getText();
        final String password = builder.toString();
        builder = (SpannableStringBuilder) confirmPasswordEditText.getText();
        final String confirm = builder.toString();
        builder = (SpannableStringBuilder) signUpNameEditText.getText();
        final String name = builder.toString();
        builder = (SpannableStringBuilder)signUpProfileEditText.getText();
        final String profileText = builder.toString();

        if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
            if(!(profileUri == null || backgroundUri == null)){
                if (password.equals(confirm)) {
                    if (!(password.length() < 6)) {
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Successfully created a new account", Toast.LENGTH_SHORT).show();

                                    DataSenderToServer sender = new DataSenderToServer();

                                    FirebaseUser user = auth.getCurrentUser();

                                    profile = new UserProfile(user.getUid(), email, name, profileText);

                                    sender.addNewUser(profile, profileUri, backgroundUri);

                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, InitialActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed created a new account", Toast.LENGTH_SHORT).show();

                                    Log.i("Failed", "Failed:" + task.getException().getMessage());
                                }
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.passwordLengthErrorMessage_text), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.passwordConfirmationNoMatchMessage_text), Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Please set a profile image", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Please fill up the form", Toast.LENGTH_SHORT).show();
        }
    }

    public void setBlankImageToSelectImage(){
        selectProfileImageButton.setImageResource(R.drawable.selectprofile);
        isImageSelected = false;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException{
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }

    public void cropImageForProfile(Uri uri){
        CropImage.activity(uri)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAutoZoomEnabled(false)
                .start(this);
        isEditingProfile = true;
    }

    public void cropImageForBackGround(Uri uri){
        CropImage.activity(uri)
                .setAspectRatio(3, 1)
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAutoZoomEnabled(false)
                .start(this);
        isEditingProfile = false;
    }

    public void resizeImage(Uri uri, boolean isProfile) throws IOException{

        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
        Bitmap original = BitmapFactory.decodeFile(uri.toString(), options);
        Bitmap resize;

        int sideLength = 100;

        FileOutputStream outputStream;

        if(isProfile){
            deleteFile(FILE_NAME_PROFILE_IMAGE);
            outputStream = openFileOutput(FILE_NAME_PROFILE_IMAGE, Context.MODE_PRIVATE);
            resize = Bitmap.createScaledBitmap(original, sideLength, sideLength, false);
        } else {
            deleteFile(FILE_NAME_BACKGROUND_IMAGE);
            outputStream = openFileOutput(FILE_NAME_BACKGROUND_IMAGE, Context.MODE_PRIVATE);
            resize = Bitmap.createScaledBitmap(original, sideLength * 3, sideLength, false);
        }

        resize.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        if (isProfile){
            profileUri = Uri.fromFile(getFileStreamPath(FILE_NAME_PROFILE_IMAGE));
        } else {
            backgroundUri = Uri.fromFile(getFileStreamPath(FILE_NAME_BACKGROUND_IMAGE));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        // TODO: 2017/11/07 requestCodeを理解する

        if (requestCode == PROFILE_IMAGE_PICK_REQUEST_CODE || requestCode ==BACKGROUND_IMAGE_PICK_REQUEST_CODE){
            if(resultData != null){
                if(requestCode == PROFILE_IMAGE_PICK_REQUEST_CODE){
                    profileUri = resultData.getData();
                    cropImageForProfile(profileUri);
                }else if (requestCode == BACKGROUND_IMAGE_PICK_REQUEST_CODE){
                    backgroundUri = resultData.getData();
                    cropImageForBackGround(backgroundUri);
                }
            }

        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(resultData);
            if (resultCode == RESULT_OK){

                if (isEditingProfile){

                    profileUri = result.getUri();

                    try{
                        resizeImage(profileUri, true);
                    }catch (Exception e){
                        Log.e("Error", e.toString());
                    }

                    try{
                        Bitmap bitmap = getBitmapFromUri(profileUri);
                        selectProfileImageButton.setImageBitmap(bitmap);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }else{

                    backgroundUri = result.getUri();

                    try{
                        resizeImage(backgroundUri, false);
                    }catch (Exception e){
                        Log.e("Error", e.toString());
                    }

                    try{
                        Bitmap bitmap = getBitmapFromUri(backgroundUri);
                        selectBackgroundButton.setImageBitmap(bitmap);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }else{
                Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
