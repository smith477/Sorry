package martin.sms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

import martin.sms.Admin.Admin;
import martin.sms.Admin.ProfileActivityAdmin;
import martin.sms.Professor.Profesor;
import martin.sms.Professor.ProfessorAllSubjectList;
import martin.sms.Professor.ProfessorSubjectList;
import martin.sms.Student.AllSubjectsList;
import martin.sms.Student.Student;
import martin.sms.Student.StudentSubjectList;


public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin;
    private FirebaseAuth.AuthStateListener authListener;
    private Context mContext;
    private String currentUserID;
    boolean isUserLoggedIn;
    private static final String TAG = "LoginActivity";
    private String t = "TAG";

    View loadingHolder;
    AVLoadingIndicatorView avi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);

        loadingHolder =  findViewById(R.id.loadingHolder);
        avi =  findViewById(R.id.indicator);

        mContext = LoginActivity.this;
        auth = FirebaseAuth.getInstance();

        avi.show();

        currentUserID = "";
        isUserLoggedIn = false;

        if (auth.getCurrentUser() != null) {
            //auth.signOut();
            isUserLoggedIn = true;
            checkUser(auth.getCurrentUser().getUid());
        }
        else
        {
            new CountDownTimer(3000,100)
            {

                @Override
                public void onTick(long l)
                {

                }

                @Override
                public void onFinish()
                {
                    loadingHolder.setVisibility(View.GONE);
                }
            }.start();

        }

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterTypes.class));
            }
        });

        setupFirebaseAuth();
        init();

    }

    private void checkUser(final String userID){

        final Query queryProf = FirebaseDatabase.getInstance().getReference()
                .child("professors")
                .orderByChild("profesorID")
                .equalTo(userID);
        queryProf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Profesor profesor = new Profesor();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
                    profesor.setProfesorID(objectMap.get("profesorID").toString());
                    currentUserID = profesor.getProfesorID();
                    if(!currentUserID.isEmpty()) {
                        if(isUserLoggedIn) {
                            //Log.d(t, String.valueOf(singleSnapshot.child("subjects").hasChildren()));
                            startActivity(new Intent(LoginActivity.this, ProfessorSubjectList.class));
                            finish();
                        }else{
                            //Log.d(t, String.valueOf(singleSnapshot.child("subjects").hasChildren()));
                            startActivity(new Intent(LoginActivity.this, ProfessorSubjectList.class));
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Query queryStudent = FirebaseDatabase.getInstance().getReference()
                .child("students")
                .orderByChild("studentID")
                .equalTo(userID);
        queryStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Student student = new Student();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    student.setStudentID(objectMap.get("studentID").toString());
                    currentUserID = student.getStudentID();
                    if(!currentUserID.isEmpty()) {
                        if(isUserLoggedIn) {
                            startActivity(new Intent(LoginActivity.this, StudentSubjectList.class));
                            finish();
                        }else{
                            startActivity(new Intent(LoginActivity.this, StudentSubjectList.class));
                            finish();
                            auth.getCurrentUser().updatePassword("password")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User password updated.");
                                                Toast.makeText(LoginActivity.this, "Password changed",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query queryAdmin = FirebaseDatabase.getInstance().getReference()
                .child("admins")
                .orderByChild("adminID")
                .equalTo(userID);
        queryAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Admin admin = new Admin();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    admin.setAdminID(objectMap.get("adminID").toString());
                    currentUserID = admin.getAdminID();
                    if(!currentUserID.isEmpty()) {
                        if(isUserLoggedIn) {
                            startActivity(new Intent(LoginActivity.this, ProfileActivityAdmin.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(LoginActivity.this, ProfileActivityAdmin.class));
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isStringNull(String string){
        if(string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

    private void init(){

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if(isStringNull(email) && isStringNull(password)){
                    Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    FirebaseUser user = auth.getCurrentUser();

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else{
                                        progressBar.setVisibility(View.GONE);
                                        try{
                                                if(user.isEmailVerified()){
                                                    Log.d(TAG, "onComplete: success. email is verified.");
                                                    checkUser(user.getUid());
                                                }else{
                                                    Toast.makeText(mContext, "Email is not verified \ncheck your email inbox.", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    auth.signOut();
                                                }

                                        }catch (NullPointerException e){
                                            Log.e(TAG, "onComplete: NullPointerException: " + e.getMessage() );
                                        }
                                    }
                                }
                            });
                }

            }
        });
    }

    private void setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                } else {}
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}

