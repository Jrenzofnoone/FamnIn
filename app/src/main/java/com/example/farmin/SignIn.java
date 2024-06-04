package com.example.farmin;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {
    private DatabaseReference userDataRef;
    private TextInputEditText metEmail,etEmail, etPassword;
    FirebaseAuth mAuth;
    private Button btnLogIn, btnCancel,btnReset;
    private TextView tvClickMe, tvForgetpassword;
    private CheckBox cbPassword;
    private Dialog forgotDialog;
    private FirebaseUser currentUser;
    private WindowManager windowManager;
    private int requestCode = 1;
    private ImageView ivShowPassword;
    private Boolean depends = true;

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            if(currentUser.isEmailVerified()){
                Intent dashboard = new Intent(getApplicationContext(), DashBoard.class);
                startActivity(dashboard);
                finish();
            } else {
                Toast.makeText(this, "Please verify your Email", Toast.LENGTH_SHORT).show();
            }
        } else {
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        alarm();
        setScheduleAlarm();
        setExactAlarm();
        requestNotificationPermission();
        createNotificationChannel();
        scheduleAlarm();
        ivShowPassword = findViewById(R.id.ivShowPassword);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        mAuth = FirebaseAuth.getInstance();
        tvClickMe = findViewById(R.id.tvClickMe);
        tvForgetpassword = findViewById(R.id.tvForgetpassword);
        userDataRef = FirebaseDatabase.getInstance().getReference().child("userData");
        btnLogIn = findViewById(R.id.btnLogIn);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        forgotDialog = new Dialog(SignIn.this);
        forgotDialog.setContentView(R.layout.dialog_forgot);
        forgotDialog.setCancelable(true);
        Window window = forgotDialog.getWindow();
        float dialogPercentageWidth = 0.8f;
        float dialogPercentageHeight = 0.4f;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = (int) (screenWidth * dialogPercentageWidth);
        layoutParams.height = (int) (screenHeight * dialogPercentageHeight);
        window.setAttributes(layoutParams);
        forgotDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialogbg));
        btnReset = forgotDialog.findViewById(R.id.btnReset);
        btnCancel = forgotDialog.findViewById(R.id.btnCancel);
        metEmail = forgotDialog.findViewById(R.id.etEmail);

        tvClickMe.setOnClickListener(view -> {
            Intent login = new Intent(getApplicationContext(), SignUp.class);
            startActivity(login);
        });
        tvForgetpassword.setOnClickListener(view -> {
            forgotDialog.show();
        });
        btnCancel.setOnClickListener(view -> {
            forgotDialog.dismiss();
        });
        btnReset.setOnClickListener(view -> {
            String userEmail = metEmail.getText().toString();
            if(TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                Toast.makeText(this, "Enter your registered Email", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(this, "Check Email", Toast.LENGTH_SHORT).show();
                    forgotDialog.dismiss();
                }else {
                    Toast.makeText(this, "Unable to Send", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnLogIn.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            if (email.isEmpty() ) {
                etEmail.setError("Cannot be Empty");
            } else if (password.isEmpty()) {
                etPassword.setError("Cannot be Empty");
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if(user.isEmailVerified()){
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                boolean userExists = false;
                                                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                    anotherUserUploads anotherUserUploads = postSnapshot.getValue(com.example.farmin.anotherUserUploads.class);
                                                    if (user.getEmail().equals(anotherUserUploads.getUserEmail())) {
                                                        userExists = true;
                                                        break;
                                                    }
                                                }
                                                if (userExists) {
                                                    Intent dashboard = new Intent(getApplicationContext(), DashBoard.class);
                                                    startActivity(dashboard);
                                                    finish();
                                                } else {
                                                    Intent setting = new Intent(getApplicationContext(), FinishSetting.class);
                                                    startActivity(setting);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                        Toast.makeText(SignIn.this, "Finish Setup", Toast.LENGTH_SHORT).show();
                                    } else  {
                                        Toast.makeText(SignIn.this, "Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if(task.getException() instanceof FirebaseAuthInvalidUserException) {
                                        Toast.makeText(SignIn.this, "Email doesn't exist", Toast.LENGTH_SHORT).show();
                                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(SignIn.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
        ivShowPassword.setOnClickListener(view -> {
            if(depends == true) {
                depends = false;
                ivShowPassword.setImageResource(R.drawable.closedeye);
                etPassword.setTransformationMethod(null);
            } else {
                depends = true;
                ivShowPassword.setImageResource(R.drawable.eye);
                etPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });
    }



    private void scheduleAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.d("checks", calendar.getTime().toString());
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Channel for my app notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("my_channel_id", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Log.d("checks", "fuck everything");
    }
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, requestCode);
            } else {
                Log.d("permission", "already given");
            }
        }
    }
    private void alarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SET_ALARM) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SET_ALARM}, requestCode);
            } else {
                Log.d("permission", "already given");
            }
        }
    }
    private void setScheduleAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, requestCode);
            } else {
                Log.d("permission", "already given");
            }
        }
    }
    private void setExactAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_EXACT_ALARM}, requestCode);
            } else {
                Log.d("permission", "already given");
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Notification permission granted");
            } else {
                Log.d("Permission", "Notification permission denied");
            }
        }
    }
}