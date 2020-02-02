package martin.sms.Professor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import martin.sms.FirebaseMethods;
import martin.sms.LoginActivity;
import martin.sms.R;
import martin.sms.RegisterTypes;
import martin.sms.Student.MainActivity;
import martin.sms.Student.RegisterActivity;

public class RegisterActivityProfessor extends AppCompatActivity {

    private static final String TAG = "RegisterActivityProf";
    private EditText inputEmail, inputPassword, inputFirstName, inputLastName;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String email, password, firstname, lastname;
    private FirebaseMethods firebaseMethods;
    private FirebaseAuth.AuthStateListener authListener;
    private Context mContext;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private String append = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_professor);
        mContext = RegisterActivityProfessor.this;
        firebaseMethods = new FirebaseMethods(mContext);
        auth = FirebaseAuth.getInstance();
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputFirstName = (EditText) findViewById(R.id.name);
        inputLastName = (EditText) findViewById(R.id.lastname);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mContext = RegisterActivityProfessor.this;
        firebaseMethods = new FirebaseMethods(mContext);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivityProfessor.this, LoginActivity.class));
            }
        });

        init();
        setupFirebaseAuth();
    }

    private void init(){

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                firstname = inputFirstName.getText().toString();
                lastname = inputLastName.getText().toString();

                if(checkInputs(email, password)){
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseMethods.registerNewEmail(email, password, "");
                }

            }
        });
    }

    private boolean checkInputs(String email, String password){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(email.equals("") || password.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    //If user is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onComplete: Successful signup");
                            firebaseMethods.addNewProfessor(firstname, lastname, email);
                            Toast.makeText(mContext, "Successful signup!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //startActivity(new Intent(RegisterActivityProfessor.this, LoginActivity.class));
                    finish();

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
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
