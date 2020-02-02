package martin.sms.Student;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import martin.sms.FirebaseMethods;
import martin.sms.LoginActivity;
import martin.sms.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText inputEmail, inputPassword, inputFirstName, inputLastName, inputIndex;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String email, password, ime, prezime, index;
    private FirebaseMethods firebaseMethods;
    private FirebaseAuth.AuthStateListener authListener;
    private Context mContext;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private boolean indexAlredayExists;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);
        mContext = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);
        auth = FirebaseAuth.getInstance();
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputIndex = (EditText) findViewById(R.id.index);
        inputFirstName = (EditText) findViewById(R.id.name);
        inputLastName = (EditText) findViewById(R.id.lastname);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mContext = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);
        indexAlredayExists = false;

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        init();
        setupFirebaseAuth();
    }

    private void init(){

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                indexAlredayExists = false;
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                index = inputIndex.getText().toString();
                ime = inputFirstName.getText().toString();
                prezime = inputLastName.getText().toString();

                checkIfIndexExists(index);
            }
        });
    }

    private boolean checkInputs(String email, String password, String index, String ime, String prezime){
        if(email.equals("") || password.equals("") || index.equals("") || ime.equals("") || prezime.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkIfIndexExists(final String index) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("students")
                .orderByChild("index")
                .equalTo(index);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Toast.makeText(mContext, "Index already exists!", Toast.LENGTH_SHORT).show();
                        indexAlredayExists = true;
                    }
                }
                //auth.signOut();
                if(checkInputs(email, password, index, ime, prezime)){
                    if(!indexAlredayExists) {
                        firebaseMethods.registerNewEmail(email, password, index);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return indexAlredayExists;
    }

    private void setupFirebaseAuth(){
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            firebaseMethods.addNewStudent(index, ime, prezime, email);
                            Toast.makeText(mContext, "Signup successful!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    finish();

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